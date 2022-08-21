package ru.job4j.todo.store;

import ru.job4j.todo.model.Task;

import java.util.List;

public interface TasksStore extends Store<Task>, AutoCloseable {

    List<Task> findAllWithCertainStatus(int status);
}