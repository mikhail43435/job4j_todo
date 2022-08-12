package ru.job4j.todo.store;

import ru.job4j.todo.model.User;

import java.util.List;
import java.util.Optional;

public class AccountStoreImpl<T extends User> implements AccountStore<T> {
    @Override
    public T add(T user) {
        return null;
    }

    @Override
    public boolean update(T users) {
        return false;
    }

    @Override
    public boolean delete(T user) {
        return false;
    }

    @Override
    public List<T> findAll() {
        return null;
    }

    @Override
    public Optional<T> findById(int id) {
        return Optional.empty();
    }

    @Override
    public Optional<T> findByLoginAndPassword(T user) {
        return Optional.empty();
    }

    @Override
    public void close() throws Exception {

    }
}
