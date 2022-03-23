package ua.com.okonsergei.repository.jdbc;

import ua.com.okonsergei.model.Label;
import ua.com.okonsergei.model.Post;
import ua.com.okonsergei.repository.PostRepository;
import ua.com.okonsergei.utils.JdbcUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JdbcPostRepositoryImpl implements PostRepository {

    private final JdbcLabelRepositoryImpl labelRepository = new JdbcLabelRepositoryImpl();
    private final JdbcUtils jdbcUtils = new JdbcUtils();

    @Override
    public List<Post> findAll() {
        Set<Post> posts = new HashSet<>();
        String sql = "SELECT posts.post_id, content, created, updated, posts_labels.label_id, labels.name FROM posts " +
                "INNER JOIN posts_labels ON posts.post_id = posts_labels.post_id" +
                " INNER JOIN labels ON posts_labels.label_id = labels.label_id";
        try (PreparedStatement preparedStatement = jdbcUtils.getPreparedStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                long postId = resultSet.getLong("post_id");
                if (posts.stream().map(Post::getId).collect(Collectors.toList()).contains(postId)) {
                    Post postTemp = posts.stream().filter(post -> post.getId().equals(postId)).findAny().get();
                    List<Label> labels = postTemp.getLabels();
                    labels.add(labelRepository.convertResultSetToLabel(resultSet));
                    postTemp.setLabels(labels);
                    posts.add(postTemp);
                } else {
                    Post post = convertResultSetToPost(resultSet);
                    List<Label> labels = new ArrayList<>();
                    labels.add(labelRepository.convertResultSetToLabel(resultSet));
                    post.setLabels(labels);
                    posts.add(post);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts.stream().toList();
    }

    @Override
    public Post findById(Long id) {
        Post post = new Post();
        String sql = "SELECT post_id, content,created,updated FROM posts  WHERE post_id=?";
        try (PreparedStatement preparedStatement = jdbcUtils.getPreparedStatement(sql)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSetPost = preparedStatement.executeQuery();

            while (resultSetPost.next()) {
                post = convertResultSetToPost(resultSetPost);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public Post save(Post post) {
        long generatedId = 0L;

        String sql = "INSERT INTO posts(content, created, updated) VALUES(?,?,?)";
        try (PreparedStatement preparedStatement = jdbcUtils.getPreparedStatementReturnId(sql)) {
            preparedStatement.setString(1, post.getContent());
            preparedStatement.setObject(2, post.getCreated());
            preparedStatement.setObject(3, post.getUpdated());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                generatedId = resultSet.getLong(1);
            }
            post.setId(generatedId);

            insertLabelsToTablePostsLabels(post);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Added post with id " + generatedId);
        return post;
    }

    @Override
    public void deleteById(Long id) {
        if (findById(id).getId() == null) {
            System.out.println("Unable to delete post from database. Post with id " + id + " not found");
        } else {
            String sql = "DELETE FROM posts WHERE post_id=?;";
            try (PreparedStatement preparedStatement = jdbcUtils.getPreparedStatement(sql)) {
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();

                System.out.println("Deleted post by id " + id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(Post post) {
        if (post.getId() == null) {
            System.out.println("Id is null. Unable to update post");
        }
        String sql = "UPDATE posts SET  content=?, updated=? WHERE post_id=?;" +
                "DELETE FROM posts_labels WHERE post_id=?";
        try (PreparedStatement preparedStatement = jdbcUtils.getPreparedStatement(sql)) {
            preparedStatement.setString(1, post.getContent());
            preparedStatement.setObject(2, post.getUpdated());
            preparedStatement.setLong(3, post.getId());
            preparedStatement.setLong(4, post.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        insertLabelsToTablePostsLabels(post);
    }

    public Post convertResultSetToPost(ResultSet resultSet) throws SQLException {
        Post post = new Post();
        post.setId(resultSet.getLong("post_id"));
        post.setContent(resultSet.getString("content"));
        post.setCreated(resultSet.getTimestamp("created").toLocalDateTime());
        post.setUpdated(resultSet.getTimestamp("updated").toLocalDateTime());

        return post;
    }

    private void insertLabelsToTablePostsLabels(Post post) {
        StringBuilder sqlInsertToPostsLabels = new StringBuilder();
        for (int i = 0; i < post.getLabels().size(); i++) {
            sqlInsertToPostsLabels.append("INSERT INTO posts_labels (post_id, label_id) VALUES (")
                    .append(post.getId()).append(",")
                    .append(post.getLabels().get(i).getId())
                    .append(")").append(";");
        }
        try (PreparedStatement preparedStatement = jdbcUtils.getPreparedStatement(sqlInsertToPostsLabels.toString())) {
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
