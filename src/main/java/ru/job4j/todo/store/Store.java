package ru.job4j.todo.store;

import java.util.List;
import java.util.Optional;

public interface Store<T> {
    T add(T user);

    boolean update(T users);

    boolean delete(T user);

    List<T> findAll();

    Optional<T> findById(int id);
}