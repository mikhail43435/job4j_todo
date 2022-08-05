package ru.job4j.todo.store;

import org.hibernate.HibernateException;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.todo.model.Task;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TaskHibernateDBStoreTest {

    private static TaskHibernateDBStore store;

    @BeforeAll
    static void init() {
        try {
            store = new TaskHibernateDBStore(new
                    MetadataSources(new StandardServiceRegistryBuilder()
                    .configure().build()).buildMetadata().buildSessionFactory());
        } catch (HibernateException exception) {
            System.out.println("Problem creating session factory");
            exception.printStackTrace();
        }
    }

    @AfterAll
    static void finish() throws Exception {
        store.close();
    }

    @Test
    void whenAddAndFindById() {
        Task taskToAdd = new Task(0, "task 2", "task 2 desc", 1, LocalDate.now());
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
        Task taskToAdd1 = new Task(0, "task 31", "task 31 desc", 1, LocalDate.now());
        store.add(taskToAdd1);
        Task taskToAdd2 = new Task(0, "task 32", "task 32 desc", 2, LocalDate.now());
        store.add(taskToAdd2);
        Task taskToAdd3 = new Task(0, "task 33", "task 33 desc", 1, LocalDate.now());
        store.add(taskToAdd3);
        List<Task> listOfNewTasks = List.of(taskToAdd1, taskToAdd2, taskToAdd3);
        list.addAll(listOfNewTasks);
        assertThat(store.findAll()).isEqualTo(list);
    }

    @Test
    void whenUpdate() {
        Task taskToAdd = new Task(0, "task 4", "task 4 desc", 1, LocalDate.now());
        store.add(taskToAdd);
        Task taskToUpdate = new Task(
                taskToAdd.getId(),
                "task 1 updated",
                "task 1 updated desc",
                2, LocalDate.now().plusDays(1));
        store.update(taskToUpdate);
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
        Task taskToAdd = new Task(0, "task 5", "task 5 desc", 1, LocalDate.now());
        store.add(taskToAdd);
        assertThat(store.findById(taskToAdd.getId())).isPresent();
        store.delete(taskToAdd);
        assertThat(store.findById(taskToAdd.getId())).isNotPresent();
    }
}