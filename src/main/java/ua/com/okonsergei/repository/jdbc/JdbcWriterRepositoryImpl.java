package ua.com.okonsergei.repository.jdbc;

import ua.com.okonsergei.model.Post;
import ua.com.okonsergei.model.Writer;
import ua.com.okonsergei.repository.WriterRepository;
import ua.com.okonsergei.utils.JdbcUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JdbcWriterRepositoryImpl implements WriterRepository {

    private final JdbcPostRepositoryImpl postRepository = new JdbcPostRepositoryImpl();
    private final JdbcUtils jdbcUtils = new JdbcUtils();

    @Override
    public List<Writer> findAll() {
        Set<Writer> writers = new HashSet<>();
        String sql = "Select writers.writer_id, first_name, last_name, writers_posts.post_id, posts.content," +
                " POSTS.created, POSTS.updated" +
                " FROM writers  inner join writers_posts " +
                "on writers.writer_id = writers_posts.writer_id inner join posts" +
                " ON writers_posts.post_id = posts.post_id";
        try (PreparedStatement preparedStatement = jdbcUtils.getPreparedStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                long writerId = resultSet.getLong("writer_id");
                if (writers.stream().map(Writer::getId).collect(Collectors.toList()).contains(writerId)) {
                    Writer writerTemp = writers.stream().filter(writer -> writer.getId().equals(writerId)).findAny().get();
                    List<Post> posts = writerTemp.getPosts();
                    posts.add(postRepository.convertResultSetToPost(resultSet));
                    writerTemp.setPosts(posts);
                    writers.add(writerTemp);
                } else {
                    Writer writer = convertResultSetToWriter(resultSet);
                    List<Post> posts = new ArrayList<>();
                    posts.add(postRepository.convertResultSetToPost(resultSet));
                    writer.setPosts(posts);
                    writers.add(writer);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return writers.stream().toList();
    }

    @Override
    public Writer findById(Long id) {
        Writer writer = new Writer();
        String sql = "SELECT writer_id,first_name, last_name FROM writers WHERE writer_id=?";
        try (PreparedStatement preparedStatement = jdbcUtils.getPreparedStatement(sql)) {
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
        String sql = "INSERT INTO writers(first_name, last_name) VALUES(?,?)";
        try (PreparedStatement preparedStatement = jdbcUtils.getPreparedStatementReturnId(sql)) {
            preparedStatement.setString(1, writer.getFirstName());
            preparedStatement.setString(2, writer.getLastName());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                generatedId = resultSet.getLong(1);
            }
            writer.setId(generatedId);
            insertWritersAndPostsToTableWritersPosts(writer);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Added writer with id " + generatedId);
        return writer;
    }

    @Override
    public void deleteById(Long id) {
        if (findById(id).getId() != null) {
            String sql = "DELETE FROM writers WHERE writer_id=?";
            try (PreparedStatement preparedStatement = jdbcUtils.getPreparedStatement(sql)) {
                preparedStatement.setLong(1, id);
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
    public void update(Writer writer) {
        if (writer.getId() == null) {
            System.out.println("Id is null. Unable to update writer");
        } else {
            String sql = "UPDATE writers SET  first_name=?, last_name=? WHERE writer_id=?;" +
                    "DELETE FROM writers_posts WHERE writer_id=?";
            try (PreparedStatement preparedStatement = jdbcUtils.getPreparedStatement(sql)) {
                preparedStatement.setString(1, writer.getFirstName());
                preparedStatement.setString(2, writer.getLastName());
                preparedStatement.setLong(3, writer.getId());
                preparedStatement.setLong(4, writer.getId());
                preparedStatement.executeUpdate();

                insertWritersAndPostsToTableWritersPosts(writer);

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

    private void insertWritersAndPostsToTableWritersPosts(Writer writer) {
        StringBuilder sqlInsertToWritersPostsTable = new StringBuilder();
        for (int i = 0; i < writer.getPosts().size(); i++) {
            sqlInsertToWritersPostsTable.append("INSERT INTO writers_posts (writer_id, post_id ) VALUES (")
                    .append(writer.getId()).append(",")
                    .append(writer.getPosts().get(i).getId())
                    .append(")").append(";");
        }
        try (PreparedStatement preparedStatement = jdbcUtils.getPreparedStatement(sqlInsertToWritersPostsTable.toString())) {
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
