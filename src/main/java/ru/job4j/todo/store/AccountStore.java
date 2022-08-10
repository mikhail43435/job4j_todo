package ru.job4j.todo.store;

import ru.job4j.todo.model.User;

import java.util.List;
import java.util.Optional;

public interface AccountStore extends AutoCloseable {

    User add(User user);

    boolean update(User users);

    boolean verify(User user);

    boolean delete(User user);

    List<User> findAll();

    Optional<User> findById(int id);

}