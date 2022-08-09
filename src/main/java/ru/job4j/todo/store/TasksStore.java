package ru.job4j.todo.store;

import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

public interface TasksStore extends AutoCloseable {

    Task add(Task task);

    boolean update(Task tasks);

    boolean delete(Task task);

    List<Task> findAll();

    Optional<Task> findById(int id);

    List<Task> findAllWithCertainStatus(int status);
}