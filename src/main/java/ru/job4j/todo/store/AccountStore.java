package ru.job4j.todo.store;

import ru.job4j.todo.model.User;

import java.util.List;
import java.util.Optional;

public interface AccountStore<T extends User> extends AutoCloseable {

    T add(T user);

    boolean update(T users);

    boolean delete(T user);

    List<T> findAll();

    Optional<T> findById(int id);

    Optional<T> findByLoginAndPassword(T user);
}