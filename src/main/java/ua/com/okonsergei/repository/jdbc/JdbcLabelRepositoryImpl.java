package ua.com.okonsergei.repository.jdbc;

import ua.com.okonsergei.model.Label;
import ua.com.okonsergei.repository.LabelRepository;
import ua.com.okonsergei.utils.JdbcUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcLabelRepositoryImpl implements LabelRepository {

    private final JdbcUtils jdbcUtils = new JdbcUtils();

    @Override
    public List<Label> findAll() {
        List<Label> labels = new ArrayList<>();
        String sql = "SELECT label_id,name FROM labels";

        try (PreparedStatement preparedStatement = jdbcUtils.getPreparedStatementReturnId(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                labels.add(convertResultSetToLabel(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return labels;
    }

    @Override
    public Label findById(Long id) {
        Label label = new Label();

        String sql = "SELECT label_id, name FROM labels WHERE label_id=?";
        try (PreparedStatement preparedStatement = jdbcUtils.getPreparedStatement(sql)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                label = convertResultSetToLabel(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return label;
    }

    @Override
    public Label save(Label label) {
        long generatedId = 0L;
        String sql = "INSERT INTO labels(name) VALUES(?)";
        try (PreparedStatement preparedStatement = jdbcUtils.getPreparedStatementReturnId(sql)) {
            preparedStatement.setString(1, label.getName());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                generatedId = resultSet.getLong(1);
            }
            label.setId(generatedId);
            System.out.println("Added new label name" + label);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return label;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM posts_labels WHERE label_id=?; DELETE FROM labels WHERE label_id=?;";
        try {
            if (findById(id).getId() != null) {
                try (PreparedStatement preparedStatement = jdbcUtils.getPreparedStatement(sql)) {
                    preparedStatement.setLong(1, id);
                    preparedStatement.setLong(2, id);
                    preparedStatement.executeUpdate();
                    System.out.println("Deleted label by id " + id);
                }
            } else {
                System.out.println("Unable to delete label from database. Label with id " + id + " not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Label label) {
        String sql = "UPDATE labels SET  name=? WHERE label_id=?";
        if (label.getId() == null) {
            System.out.println("Id is null. Unable to update label");
        }
        try (PreparedStatement preparedStatement = jdbcUtils.getPreparedStatement(sql)) {
            preparedStatement.setString(1, label.getName());
            preparedStatement.setLong(2, label.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Label convertResultSetToLabel(ResultSet resultSet) throws SQLException {
        Label label = new Label();
        label.setId(resultSet.getLong("label_id"));
        label.setName(resultSet.getString("name"));

        return label;
    }
}

