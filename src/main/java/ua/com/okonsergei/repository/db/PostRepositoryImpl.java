package ua.com.okonsergei.repository.db;

import ua.com.okonsergei.model.Label;
import ua.com.okonsergei.model.Post;
import ua.com.okonsergei.repository.PostRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostRepositoryImpl implements PostRepository {

    private final LabelRepositoryImpl labelRepository = new LabelRepositoryImpl();

    @Override
    public List<Post> findAll() {
        List<Post> posts = new ArrayList<>();

        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatementPosts = connection
                     .prepareStatement("SELECT posts.post_id, content,created,updated FROM posts",
                             ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             PreparedStatement preparedStatementLabels = connection
                     .prepareStatement("SELECT  posts_labels.post_id, posts_labels.label_id, labels.name " +
                             "FROM labels JOIN posts_labels ON posts_labels.label_id=labels.label_id")) {

            ResultSet resultSetPosts = preparedStatementPosts.executeQuery();

            while (resultSetPosts.next()) {
                posts.add(convertResultSetToPost(resultSetPosts));
            }

            ResultSet resultSetLabels = preparedStatementLabels.executeQuery();

            Map<Long, List<Label>> postIdLabelIds = new HashMap<>();

            while (resultSetLabels.next()) {
                Long id = resultSetLabels.getLong("post_id");
                List<Label> labels = new ArrayList<>();

                if (postIdLabelIds.containsKey(id))
                    labels = postIdLabelIds.get(id);

                Label label = labelRepository.convertResultSetToLabel(resultSetLabels);
                labels.add(label);

                postIdLabelIds.put(id, labels);
            }
            for (Post post : posts) {
                if (postIdLabelIds.containsKey(post.getId())) {
                    post.setLabels(postIdLabelIds.get(post.getId()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Post findById(Long id) {
        Post post = new Post();

        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatementPost = connection
                     .prepareStatement("SELECT post_id, content,created,updated FROM posts  WHERE post_id=?");
             PreparedStatement preparedStatementLabel = connection
                     .prepareStatement("SELECT  labels.name, posts_labels.label_id FROM labels JOIN posts_labels " +
                             "ON posts_labels.label_id=labels.label_id WHERE post_id=?")) {

            preparedStatementPost.setLong(1, id);
            ResultSet resultSetPost = preparedStatementPost.executeQuery();

            preparedStatementLabel.setLong(1, id);
            ResultSet resultSetLabel = preparedStatementLabel.executeQuery();

            while (resultSetPost.next()) {
                post = convertResultSetToPost(resultSetPost);
            }

            List<Label> labels = new ArrayList<>();
            while (resultSetLabel.next()) {
                labels.add(labelRepository.convertResultSetToLabel(resultSetLabel));
            }
            post.setLabels(labels);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public Post save(Post post) {
        long generatedId = 0L;

        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("INSERT INTO posts(content, created, updated) VALUES(?,?,?)",
                             Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, post.getContent());
            preparedStatement.setObject(2, post.getCreated());
            preparedStatement.setObject(3, post.getUpdated());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                generatedId = resultSet.getLong(1);
            }
            insertLabelsToTablePostsLabels(generatedId, post);

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
            try (Connection connection = DataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement
                         ("DELETE FROM posts_labels WHERE post_id=?; DELETE FROM posts WHERE post_id=?;")) {

                preparedStatement.setLong(1, id);
                preparedStatement.setLong(2, id);
                preparedStatement.executeUpdate();

                System.out.println("Deleted post by id " + id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(Long id, Post post) {
        if (id == null) {
            System.out.println("Id is null. Unable to update post");
        }
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("UPDATE posts SET  content=?, updated=? WHERE post_id=?;" +
                             "DELETE FROM posts_labels WHERE post_id=?")) {

            preparedStatement.setString(1, post.getContent());
            preparedStatement.setObject(2, post.getUpdated());
            preparedStatement.setLong(3, post.getId());
            preparedStatement.setLong(4, post.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        insertLabelsToTablePostsLabels(id, post);
    }

    public Post convertResultSetToPost(ResultSet resultSet) throws SQLException {
        Post post = new Post();
        post.setId(resultSet.getLong("post_id"));
        post.setContent(resultSet.getString("content"));
        post.setCreated(resultSet.getTimestamp("created").toLocalDateTime());
        post.setUpdated(resultSet.getTimestamp("updated").toLocalDateTime());

        return post;
    }

    private void insertLabelsToTablePostsLabels(Long id, Post post) {
        StringBuilder sqlInsertToPostsLabels = new StringBuilder();
        for (int i = 0; i < post.getLabels().size(); i++) {
            sqlInsertToPostsLabels.append("INSERT INTO posts_labels (post_id, label_id) VALUES (")
                    .append(id).append(",")
                    .append(post.getLabels().get(i).getId())
                    .append(")").append(";");
        }
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement(sqlInsertToPostsLabels.toString())) {
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
