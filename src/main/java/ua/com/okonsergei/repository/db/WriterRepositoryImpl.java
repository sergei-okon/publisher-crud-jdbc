package ua.com.okonsergei.repository.db;

import ua.com.okonsergei.model.Post;
import ua.com.okonsergei.model.Writer;
import ua.com.okonsergei.repository.WriterRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriterRepositoryImpl implements WriterRepository {

    private final PostRepositoryImpl postRepository = new PostRepositoryImpl();

    @Override
    public List<Writer> findAll() {
        List<Writer> writers = new ArrayList<>();
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT writer_id, first_name, last_name FROM writers");

             PreparedStatement preparedStatementPosts = connection
                     .prepareStatement("SELECT writers_posts.writer_id, writers_posts.post_id,  posts.content," +
                             " posts.created, posts.updated" +
                             " FROM posts JOIN writers_posts ON writers_posts.post_id=posts.post_id")) {

            ResultSet resultSetWriters = preparedStatement.executeQuery();
            while (resultSetWriters.next()) {
                writers.add(convertResultSetToWriter(resultSetWriters));
            }

            ResultSet resultSetPosts = preparedStatementPosts.executeQuery();

            Map<Long, List<Post>> writerIdPostIds = new HashMap<>();

            while (resultSetPosts.next()) {
                Long id = resultSetPosts.getLong("writer_id");
                List<Post> posts = new ArrayList<>();
                if (writerIdPostIds.containsKey(id))
                    posts = writerIdPostIds.get(id);

                Post post = postRepository.convertResultSetToPost(resultSetPosts);
                posts.add(post);

                writerIdPostIds.put(id, posts);
            }
            for (Writer writer : writers) {
                if (writerIdPostIds.containsKey(writer.getId())) {
                    writer.setPosts(writerIdPostIds.get(writer.getId()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return writers;
    }

    @Override
    public Writer findById(Long id) {
        Writer writer = new Writer();

        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT writer_id,first_name, last_name FROM writers WHERE writer_id=?")) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                writer = convertResultSetToWriter(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return writer;
    }

    @Override
    public Writer save(Writer writer) {
        long generatedId = 0L;

        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("INSERT INTO writers(first_name, last_name) VALUES(?,?)",
                             Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, writer.getFirstName());
            preparedStatement.setString(2, writer.getLastName());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                generatedId = resultSet.getLong(1);
            }
            insertWritersAndPostsToTableWritersPosts(generatedId, writer);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Added writer with id " + generatedId);
        return writer;
    }

    @Override
    public void deleteById(Long id) {
        if (findById(id).getId() != null) {
            try (Connection connection = DataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement
                         ("DELETE FROM writers_posts WHERE writer_id=?; DELETE FROM writers WHERE writer_id=?;")) {

                preparedStatement.setLong(1, id);
                preparedStatement.setLong(2, id);
                preparedStatement.executeUpdate();

                System.out.println("Deleted writer by id " + id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Unable to delete writer from database. " + "Writer with id " + id + " not found");
        }
    }

    @Override
    public void update(Long id, Writer writer) {
        if (id == null) {
            System.out.println("Id is null. Unable to update writer");
        } else {
            try (Connection connection = DataSource.getConnection();
                 PreparedStatement preparedStatement = connection
                         .prepareStatement("UPDATE writers SET  first_name=?, last_name=? WHERE writer_id=?;" +
                                 "DELETE FROM writers_posts WHERE writer_id=?")) {

                preparedStatement.setString(1, writer.getFirstName());
                preparedStatement.setString(2, writer.getLastName());
                preparedStatement.setLong(3, writer.getId());
                preparedStatement.setLong(4, writer.getId());
                preparedStatement.executeUpdate();

                insertWritersAndPostsToTableWritersPosts(id, writer);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private Writer convertResultSetToWriter(ResultSet resultSet) throws SQLException {
        Writer writer = new Writer();
        writer.setId(resultSet.getLong("writer_id"));
        writer.setFirstName(resultSet.getString("first_name"));
        writer.setLastName(resultSet.getString("last_name"));

        return writer;
    }

    private void insertWritersAndPostsToTableWritersPosts(Long id, Writer writer) {
        StringBuilder sqlInsertToWritersPostsTable = new StringBuilder();
        for (int i = 0; i < writer.getPosts().size(); i++) {
            sqlInsertToWritersPostsTable.append("INSERT INTO writers_posts (writer_id, post_id ) VALUES (")
                    .append(id).append(",")
                    .append(writer.getPosts().get(i).getId())
                    .append(")").append(";");
        }
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement(sqlInsertToWritersPostsTable.toString())) {
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
