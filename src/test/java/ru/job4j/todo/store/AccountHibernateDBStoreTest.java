package ru.job4j.todo.store;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.LoggerService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AccountHibernateDBStoreTest {

    private static AccountHibernateDBStore store;

    @BeforeAll
    static void init() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        SessionFactory sessionFactory = new MetadataSources(registry).
                buildMetadata().
                buildSessionFactory();

        try {
            store = new AccountHibernateDBStore(sessionFactory);
        } catch (HibernateException e) {
            LoggerService.LOGGER.error(
                    "Exception in creating session factory in AccountHibernateDBStore.init method",
                    e);
        }

        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.createSQLQuery("TRUNCATE TABLE users;").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LoggerService.LOGGER.error("Exception in AccountHibernateDBStore.init method", e);
        } finally {
            session.close();
        }
    }

    @AfterAll
    static void finish() throws Exception {
        store.close();
    }

    @Test
    void whenAddAndFindById() {
        User userToAdd = new User(0, "user 1", "user 1 login", "user1password");
        User userReturned = store.add(userToAdd);
        assertThat(userReturned.getId()).isEqualTo(userToAdd.getId());
        assertThat(userReturned.getName()).isEqualTo(userToAdd.getName());
        assertThat(userReturned.getLogin()).isEqualTo(userToAdd.getLogin());
        assertThat(userReturned.getPassword()).isEqualTo(userToAdd.getPassword());

        Optional<User> userFromDB = store.findById(userToAdd.getId());
        assertThat(userFromDB).isPresent();
        assertThat(userFromDB.get().getId()).isEqualTo(userToAdd.getId());
        assertThat(userFromDB.get().getName()).isEqualTo(userToAdd.getName());
        assertThat(userFromDB.get().getLogin()).isEqualTo(userToAdd.getLogin());
        assertThat(userFromDB.get().getPassword()).isEqualTo(userToAdd.getPassword());
    }

    @Test
    void whenFindAll() {
        List<User> listOfAllUsers = store.findAll();
        User userToAdd1 = new User(0, "user 21", "user 21 login", "user21password");
        store.add(userToAdd1);
        User userToAdd2 = new User(0, "user 22", "user 22 login", "user22password");
        store.add(userToAdd2);
        User userToAdd3 = new User(0, "user 23", "user 23 login", "user23password");
        store.add(userToAdd3);
        List<User> listOfNewUsers = List.of(userToAdd1, userToAdd2, userToAdd3);
        listOfAllUsers.addAll(listOfNewUsers);
        assertThat(store.findAll()).isEqualTo(listOfAllUsers);
    }

    @Test
    void whenUpdate() {
        User userToAdd = new User(0, "user 3", "user 3 login", "user3password");
        store.add(userToAdd);
        User userToUpdate = new User(
                userToAdd.getId(),
                "user 3 updated",
                "user 3 login updated",
                "user3password updated");

        System.out.println("888888888888888888888888888888888888888888888888888888888");
        System.out.println(userToAdd);
        System.out.println(userToUpdate);
        //assertThat(store.update(userToUpdate)).isTrue();
        store.update(userToUpdate);

        Optional<User> userFromDBAfterUpdate = store.findById(userToAdd.getId());
        assertThat(userFromDBAfterUpdate).isPresent();

        assertThat(userFromDBAfterUpdate.get().getLogin()).isEqualTo(userToAdd.getLogin());

        assertThat(userFromDBAfterUpdate.get().getId()).isEqualTo(userToUpdate.getId());
        assertThat(userFromDBAfterUpdate.get().getName()).isEqualTo(userToUpdate.getName());
        assertThat(userFromDBAfterUpdate.get().getPassword()).isEqualTo(userToAdd.getPassword());
        System.out.println(store.findAll());
    }

    @Test
    void whenDelete() {
        User userToAdd = new User(0, "user 4", "user 4 login", "user4password");
        store.add(userToAdd);
        assertThat(store.findById(userToAdd.getId())).isPresent();
        assertThat(store.delete(userToAdd)).isTrue();
        assertThat(store.findById(userToAdd.getId())).isNotPresent();
    }

    @Test
    void whenVerify() {
        User userToAdd = new User(0, "user 5", "user 5 login", "user5password");
        store.add(userToAdd);
        User userForVerify = new User(-1,
                "user 5 distinct name",
                "user 5 login",
                "user5password");
        System.out.println(store.findAll().toString());
        assertThat(store.verify(userForVerify)).isTrue();
    }
}