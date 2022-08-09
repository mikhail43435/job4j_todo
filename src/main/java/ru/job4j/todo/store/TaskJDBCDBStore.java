package ru.job4j.todo.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.exception.AddNewTaskException;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.LoggerService;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TaskJDBCDBStore implements TasksStore {

    private final BasicDataSource pool;

    public TaskJDBCDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    @Override
    public Task add(Task task) {
        String param = "INSERT INTO tasks(name, description, status, created) VALUES (?,?,?,?)";
        try (var connection = pool.getConnection();
             PreparedStatement prepareStatement =
                     connection.prepareStatement(param, PreparedStatement.RETURN_GENERATED_KEYS)) {
            prepareStatement.setString(1, task.getName());
            prepareStatement.setString(2, task.getDescription());
            prepareStatement.setInt(3, task.getStatus());
            prepareStatement.setDate(4, Date.valueOf(task.getCreated()));
            prepareStatement.execute();
            try (ResultSet resultSet = prepareStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    task.setId((resultSet.getInt(1)));
                }
            }
        } catch (Exception e) {
            LoggerService.LOGGER.error("Exception in TaskDBStore.add method", e);
            throw new AddNewTaskException("Exception in TaskDBStore.add method", e);
        }
        return task;
    }

    @Override
    public boolean update(Task task) {
        boolean result = false;
        try (var connection = pool.getConnection();
             var prepareStatement = connection.prepareStatement("UPDATE tasks SET"
                     + " name = ?,"
                     + " description = ?,"
                     + " status = ?"
                     + "WHERE id = ?")) {
            prepareStatement.setString(1, task.getName());
            prepareStatement.setString(2, task.getDescription());
            prepareStatement.setInt(3, task.getStatus());
            prepareStatement.setInt(4, task.getId());
            prepareStatement.execute();
            result = true;
        } catch (Exception e) {
            LoggerService.LOGGER.error("Exception in TaskDBStore.update method", e);
        }
        return result;
    }

    @Override
    public boolean delete(Task task) {
        boolean result = false;
        try (var connection = pool.getConnection();
             var prepareStatement =
                     connection.prepareStatement("DELETE FROM tasks WHERE id = ? ")) {
            prepareStatement.setInt(1, task.getId());
            prepareStatement.execute();
            result = true;
        } catch (Exception e) {
            LoggerService.LOGGER.error("Exception in TaskDBStore.update method", e);
        }
        return result;
    }

    @Override
    public Optional<Task> findById(int id) {
        Optional<Task> result = Optional.empty();
        try (var connection = pool.getConnection();
             var prepareStatement = connection.prepareStatement("SELECT * FROM tasks WHERE id = ?")
        ) {
            prepareStatement.setInt(1, id);
            try (var resultSet = prepareStatement.executeQuery()) {
                if (resultSet.next()) {
                    result = Optional.of(
                            new Task(resultSet.getInt("id"),
                                    resultSet.getString("name"),
                                    resultSet.getString("description"),
                                    resultSet.getInt("status"),
                                    LocalDate.ofInstant(
                                            resultSet.getTimestamp("created").toInstant(),
                                            ZoneId.systemDefault())
                            ));
                }
            }
        } catch (Exception e) {
            LoggerService.LOGGER.error("Exception in TaskDBStore.findById method", e);
        }
        return result;
    }

    @Override
    public List<Task> findAll() {
        List<Task> result = new ArrayList<>();
        try (var connection = pool.getConnection();
             var prepareStatement = connection.prepareStatement("SELECT * FROM tasks ORDER BY id")
        ) {
            try (var resultSet = prepareStatement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(new Task(resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getInt("status"),
                            LocalDate.ofInstant(
                                    resultSet.getTimestamp("created").toInstant(),
                                    ZoneId.systemDefault())
                    ));
                }
            }
        } catch (Exception e) {
            LoggerService.LOGGER.error("Exception in TaskDBStore.findAll method", e);
        }
        return result;
    }

    @Override
    public List<Task> findAllWithCertainStatus(int status) {
        List<Task> result = new ArrayList<>();
        try (var connection = pool.getConnection();
             var prepareStatement =
                     connection.prepareStatement("SELECT * FROM tasks AS t "
                             + "WHERE t.status = ? ORDER BY id;")
        ) {
            prepareStatement.setInt(1, status);
            try (var resultSet = prepareStatement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(new Task(resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getInt("status"),
                            LocalDate.ofInstant(
                                    resultSet.getTimestamp("created").toInstant(),
                                    ZoneId.systemDefault())
                    ));
                }
            }
        } catch (Exception e) {
            LoggerService.LOGGER.error(
                    "Exception in TaskDBStore.findAllWithCertainStatus method",
                    e);
        }
        return result;
    }

    @Override
    public void close() throws Exception {
        pool.close();
    }
}