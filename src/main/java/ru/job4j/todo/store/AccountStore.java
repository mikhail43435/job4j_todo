package ru.job4j.todo.store;

import ru.job4j.todo.model.User;

import java.util.Optional;

public interface AccountStore<T extends User> extends Store<T>, AutoCloseable {

    Optional<T> findByLoginAndPassword(T user);
}