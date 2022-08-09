package ru.job4j.todo.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;
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
public class AccountJDBCDBStore  {
/*
    private final BasicDataSource pool;

    public AccountJDBCDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    @Override
    public User add(User user) {
        String param = "INSERT INTO users(name, description, status, created) VALUES (?,?,?,?)";
        try (var connection = pool.getConnection();
             PreparedStatement prepareStatement =
                     connection.prepareStatement(param, PreparedStatement.RETURN_GENERATED_KEYS)) {
            prepareStatement.setString(1, user.getName());
            prepareStatement.setString(2, user.getDescription());
            prepareStatement.setInt(3, user.getStatus());
            prepareStatement.setDate(4, Date.valueOf(user.getCreated()));
            prepareStatement.execute();
            try (ResultSet resultSet = prepareStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    user.setId((resultSet.getInt(1)));
                }
            }
        } catch (Exception e) {
            LoggerService.LOGGER.error("Exception in UserDBStore.add method", e);
            throw new AddNewUserException("Exception in UserDBStore.add method", e);
        }
        return user;
    }

    @Override
    public boolean update(User user) {
        boolean result = false;
        try (var connection = pool.getConnection();
             var prepareStatement = connection.prepareStatement("UPDATE users SET"
                     + " name = ?,"
                     + " description = ?,"
                     + " status = ?"
                     + "WHERE id = ?")) {
            prepareStatement.setString(1, user.getName());
            prepareStatement.setString(2, user.getDescription());
            prepareStatement.setInt(3, user.getStatus());
            prepareStatement.setInt(4, user.getId());
            prepareStatement.execute();
            result = true;
        } catch (Exception e) {
            LoggerService.LOGGER.error("Exception in UserDBStore.update method", e);
        }
        return result;
    }

    @Override
    public boolean verify(User user) {
        return false;
    }

    @Override
    public boolean delete(User user) {
        boolean result = false;
        try (var connection = pool.getConnection();
             var prepareStatement =
                     connection.prepareStatement("DELETE FROM users WHERE id = ? ")) {
            prepareStatement.setInt(1, user.getId());
            prepareStatement.execute();
            result = true;
        } catch (Exception e) {
            LoggerService.LOGGER.error("Exception in UserDBStore.update method", e);
        }
        return result;
    }

    @Override
    public Optional<User> findById(int id) {
        Optional<User> result = Optional.empty();
        try (var connection = pool.getConnection();
             var prepareStatement = connection.prepareStatement("SELECT * FROM users WHERE id = ?")
        ) {
            prepareStatement.setInt(1, id);
            try (var resultSet = prepareStatement.executeQuery()) {
                if (resultSet.next()) {
                    result = Optional.of(
                            new User(resultSet.getInt("id"),
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
            LoggerService.LOGGER.error("Exception in UserDBStore.findById method", e);
        }
        return result;
    }

    @Override
    public List<User> findAll() {
        List<User> result = new ArrayList<>();
        try (var connection = pool.getConnection();
             var prepareStatement = connection.prepareStatement("SELECT * FROM users ORDER BY id")
        ) {
            try (var resultSet = prepareStatement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(new User(resultSet.getInt("id"),
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
            LoggerService.LOGGER.error("Exception in UserDBStore.findAll method", e);
        }
        return result;
    }

    @Override
    public List<User> findAllWithCertainStatus(int status) {
        List<User> result = new ArrayList<>();
        try (var connection = pool.getConnection();
             var prepareStatement =
                     connection.prepareStatement("SELECT * FROM users AS t "
                             + "WHERE t.status = ? ORDER BY id;")
        ) {
            prepareStatement.setInt(1, status);
            try (var resultSet = prepareStatement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(new User(resultSet.getInt("id"),
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
                    "Exception in UserDBStore.findAllWithCertainStatus method",
                    e);
        }
        return result;
    }

    @Override
    public void close() throws Exception {
        pool.close();
    }*/
}