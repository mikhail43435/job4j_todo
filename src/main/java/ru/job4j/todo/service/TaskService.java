package ru.job4j.todo.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.store.TaskHibernateDBStore;

import java.util.List;
import java.util.Optional;

@ThreadSafe
@Service
public class TaskService {

    private final TaskHibernateDBStore store;

    public TaskService(TaskHibernateDBStore store) {
        this.store = store;
    }

    public Task add(Task task) {
        return store.add(task);
    }

    public boolean update(Task task) {
        return store.update(task);
    }

    public boolean delete(Task task) {
        return store.delete(task);
    }

    public List<Task> findAll() {
        return store.findAll();
    }

    public Optional<Task> findById(int id) {
        return store.findById(id);
    }

    public void close() throws Exception {
        store.close();
    }
}