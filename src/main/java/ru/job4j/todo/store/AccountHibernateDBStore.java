package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.exception.UserWithSameLoginAlreadyExistsException;
import ru.job4j.todo.model.User;
import ru.job4j.todo.model.UserWithoutPassword;
import ru.job4j.todo.service.LoggerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AccountHibernateDBStore implements AccountStore, AutoCloseable {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sessionFactory;

    public AccountHibernateDBStore(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User add(User user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (e.getMessage().
                    contains("org.hibernate.exception.ConstraintViolationException: ")) {
                throw new UserWithSameLoginAlreadyExistsException("User with login <"
                + user.getLogin()
                + "> already exists.");
            }
            LoggerService.LOGGER.error("Exception in AccountHibernateDBStore.add method", e);
        } finally {
            session.close();
        }
        secureUserPassword(user);
        return user;
    }

    private void secureUserPassword(User user) {
        char[] password = user.getPassword();
        for (int i = 0; i < password.length; i++) {
            password[i] = '@';
        }
    }

    /**
     * Update only NAME and PASSWORD fields after verification that such user exists
     */
    @Override
    public boolean update(User user) {
        boolean result = false;
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            int executeUpdateResult =
                    session.createQuery(
                            "update User u set "
                                    + "u.name = :fname, "
                                    + "u.password = :fpassword "
                                    + "where u.id = :fid").
                            setParameter("fname", user.getName()).
                            setParameter("fpassword", user.getPassword()).
                            setParameter("fid", user.getId()).
                            executeUpdate();
            transaction.commit();
            if (executeUpdateResult == 1) {
                result = true;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LoggerService.LOGGER.error("Exception in AccountHibernateDBStore.update method", e);
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public boolean delete(User user) {
        boolean result = false;
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            int executeUpdateResult = session.createQuery("delete from User where id = :id")
                    .setParameter("id", user.getId())
                    .executeUpdate();
            transaction.commit();
            if (executeUpdateResult == 1) {
                result = true;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LoggerService.LOGGER.error("Exception in AccountHibernateDBStore.delete method", e);
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public List<User> findAll() {
        List<User> result = new ArrayList<>();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Query<UserWithoutPassword> query = session.createQuery("from User u order by u.id");
            result.addAll(query.list());
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LoggerService.LOGGER.error("Exception in AccountHibernateDBStore.findAll method", e);
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public Optional<User> findById(int id) {
        Optional<User> result = Optional.empty();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Query query =
                    session.createQuery(
                            "from User i where i.id = :fid").
                            setParameter("fid", id);
            if (query.uniqueResult() != null) {
                result = Optional.of((UserWithoutPassword) query.uniqueResult());
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LoggerService.LOGGER.error("Exception in AccountHibernateDBStore.findById method", e);
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public Optional<UserWithoutPassword> findByLoginAndPassword(User user) {
        Optional<UserWithoutPassword> result = Optional.empty();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Query query =
                    session.createQuery(
                            "from User u where u.login = :flogin and u.password = :fpassword").
                            setParameter("flogin", user.getLogin()).
                            setParameter("fpassword", user.getPassword());
            if (query.uniqueResult() != null) {
                result = Optional.of((UserWithoutPassword) query.uniqueResult());
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LoggerService.LOGGER.error(
                    "Exception in AccountHibernateDBStore.findByLoginAndPassword method", e);
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}