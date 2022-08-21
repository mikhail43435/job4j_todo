package ru.job4j.todo.store;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.todo.Main;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.TaskStatus;
import ru.job4j.todo.service.LoggerService;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TaskJDBCDBStoreTest {

    private static TasksStore store;

    @BeforeAll
    static void init() {
        store = new TaskJDBCDBStore(new Main().loadPool());
        try (var connection = new Main().loadPool().getConnection();
             PreparedStatement prepareStatement =
                     connection.prepareStatement("DELETE FROM tasks")) {
            prepareStatement.execute();
        } catch (Exception e) {
            LoggerService.LOGGER.error("Exception in CustomerDBStoreTest.init method", e);
        }
    }

    @AfterAll
    static void finish() throws Exception {
        store.close();
    }

    @Test
    void whenAddAndFindById() {
        Task taskToAdd = new Task(0,
                "task 2",
                "task 2 desc",
                TaskStatus.NEW,
                LocalDate.now());
        store.add(taskToAdd);
        Optional<Task> taskFromDB = store.findById(taskToAdd.getId());
        assertThat(taskFromDB).isPresent();
        assertThat(taskFromDB.get().getId()).isEqualTo(taskToAdd.getId());
        assertThat(taskFromDB.get().getName()).isEqualTo(taskToAdd.getName());
        assertThat(taskFromDB.get().getDescription()).isEqualTo(taskToAdd.getDescription());
        assertThat(taskFromDB.get().getCreated()).isEqualTo(taskToAdd.getCreated());
    }

    @Test
    void whenFindAll() {
        List<Task> list = store.findAll();
        Task taskToAdd1 = new Task(0,
                "task 31",
                "task 31 desc",
                TaskStatus.NEW,
                LocalDate.now());
        store.add(taskToAdd1);
        Task taskToAdd2 = new Task(0,
                "task 32",
                "task 32 desc",
                TaskStatus.FINISHED,
                LocalDate.now());
        store.add(taskToAdd2);
        Task taskToAdd3 = new Task(0,
                "task 33",
                "task 33 desc",
                TaskStatus.NEW,
                LocalDate.now());
        store.add(taskToAdd3);
        List<Task> listOfNewTasks = List.of(taskToAdd1, taskToAdd2, taskToAdd3);
        list.addAll(listOfNewTasks);
        assertThat(store.findAll()).isEqualTo(list);
    }

    @Test
    void whenFindWithCertainStatus() {
        List<Task> list = store.findAll();
        Task taskToAdd1 = new Task(0,
                "task 321",
                "task 321 desc",
                TaskStatus.NEW,
                LocalDate.now());
        store.add(taskToAdd1);
        Task taskToAdd2 = new Task(0,
                "task 322",
                "task 322 desc",
                TaskStatus.NEW,
                LocalDate.now());
        store.add(taskToAdd2);
        List<Task> listOfNewTasks = List.of(taskToAdd1, taskToAdd2);
        list.addAll(listOfNewTasks);
        assertThat(store.findAll()).isEqualTo(list);
    }

    @Test
    void whenUpdate() {
        Task taskToAdd = new Task(0,
                "task 4",
                "task 4 desc",
                TaskStatus.NEW,
                LocalDate.now());
        store.add(taskToAdd);
        Task taskToUpdate = new Task(
                taskToAdd.getId(),
                "task 1 updated",
                "task 1 updated desc",
                TaskStatus.FINISHED, LocalDate.now().plusDays(1));
        assertThat(store.update(taskToUpdate)).isTrue();
        Optional<Task> taskFromDBAfterUpdate = store.findById(taskToAdd.getId());
        assertThat(taskFromDBAfterUpdate).isPresent();
        assertThat(taskFromDBAfterUpdate.get().getId()).isEqualTo(taskToUpdate.getId());
        assertThat(taskFromDBAfterUpdate.get().getName()).isEqualTo(taskToUpdate.getName());
        assertThat(taskFromDBAfterUpdate.get().getDescription()).
                isEqualTo(taskToUpdate.getDescription());
        assertThat(taskFromDBAfterUpdate.get().getCreated()).isEqualTo(taskToAdd.getCreated());
    }

    @Test
    void whenDelete() {
        Task taskToAdd = new Task(0,
                "task 5",
                "task 5 desc",
                TaskStatus.NEW,
                LocalDate.now());
        store.add(taskToAdd);
        assertThat(store.findById(taskToAdd.getId())).isPresent();
        assertThat(store.delete(taskToAdd)).isTrue();
        assertThat(store.findById(taskToAdd.getId())).isNotPresent();
    }
}