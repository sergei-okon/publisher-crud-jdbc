package ua.com.okonsergei.repository.db;

import ua.com.okonsergei.model.Label;
import ua.com.okonsergei.repository.LabelRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LabelRepositoryImpl implements LabelRepository {

    @Override
    public List<Label> findAll() {
        List<Label> labels = new ArrayList<>();

        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT label_id,name FROM labels")) {
            ResultSet resultSet = preparedStatement.executeQuery();

            labels = convertResultSetToLabels(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return labels;
    }

    @Override
    public Label findById(Long id) {
        Label label = new Label();

        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT label_id, name FROM labels WHERE label_id=?")) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                label.setId(resultSet.getLong("label_id"));
                label.setName(resultSet.getString("name"));
            }
            System.out.println(label);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return label;
    }

    @Override
    public Label save(Label label) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("INSERT INTO labels(name) VALUES(?)")) {

            preparedStatement.setString(1, label.getName());
            preparedStatement.executeUpdate();

            System.out.println("Added new label named" + label.getName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return label;
    }

    @Override
    public void deleteById(Long id) {
        if (findById(id) != null) {
            try (Connection connection = DataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement
                         ("DELETE FROM posts_labels WHERE label_id=?; DELETE FROM labels WHERE label_id=?;")) {

                preparedStatement.setLong(1, id);
                preparedStatement.setLong(2, id);

                preparedStatement.executeUpdate();

                System.out.println("Deleted label by id " + id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Unable to delete label from database. Label with id " + id + " not found");
        }
    }

    @Override
    public void update(Long id, Label label) {
        if (id == null) {
            System.out.println("Id is null. Unable to update label");
        }
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("UPDATE labels SET  name=? WHERE label_id=?")) {
            preparedStatement.setString(1, label.getName());
            preparedStatement.setLong(2, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Label> convertResultSetToLabels(ResultSet resultSet) throws SQLException {
        List<Label> labels = new ArrayList<>();

        while (resultSet.next()) {
            Label label = new Label();
            label.setId(resultSet.getLong("label_id"));
            label.setName(resultSet.getString("name"));
            labels.add(label);
        }
        return labels;
    }
}

