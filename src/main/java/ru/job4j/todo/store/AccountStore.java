package ru.job4j.todo.store;

import ru.job4j.todo.model.User;

import java.util.Optional;

public interface AccountStore extends Store<User>, AutoCloseable {

    Optional<User> findByLoginAndPassword(User user);
}