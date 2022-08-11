package ru.job4j.todo.store;

import ru.job4j.todo.model.User;

import java.util.List;
import java.util.Optional;

public interface AccountStore extends AutoCloseable {

    User add(User user);

    boolean update(User users);

    boolean delete(User user);

    List<? extends User> findAll();

    Optional<? extends User> findById(int id);

    Optional<? extends User> findByLoginAndPassword(User user);
}