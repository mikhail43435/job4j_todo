package ru.job4j.todo.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.exception.AddNewTaskException;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.store.TaskHibernateDBStore;

import java.util.List;
import java.util.Optional;

@ThreadSafe
@Service
public class TaskService {

    private final TaskHibernateDBStore store;
    private final ValidationService validationService;

    public TaskService(TaskHibernateDBStore store,
                       ValidationService validationService) {
        this.store = store;
        this.validationService = validationService;
    }

    public Task add(Task task) {
        String validationResult = validationService.validateTaskName(task.getName());
        if (!validationResult.isEmpty()) {
            throw new IllegalArgumentException(validationResult);
        }
        try {
            store.add(task);
        } catch (Exception e) {
            throw new AddNewTaskException("Error in DB occurred while adding new task");
        }
        return task;
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

    public List<Task> findAllWithCertainStatus(int status) {
        return store.findAllWithCertainStatus(status);
    }

    public Optional<Task> findById(int id) {
        return store.findById(id);
    }

    public void close() throws Exception {
        store.close();
    }
}