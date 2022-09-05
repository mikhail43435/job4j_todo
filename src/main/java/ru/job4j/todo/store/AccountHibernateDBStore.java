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
import ru.job4j.todo.service.LoggerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.ToIntFunction;

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
        return user;
    }

    @Override
    public boolean update(User user) {
        return this.booleanReturnQueryFunc(
                session -> session.createQuery(
                            "update User u set "
                                    + "u.name = :fname, "
                                    + "u.password = :fpassword "
                                    + "where u.id = :fid").
                            setParameter("fname", user.getName()).
                            setParameter("fpassword", user.getPassword()).
                            setParameter("fid", user.getId()).
                            executeUpdate(),
                "delete");
    }

    @Override
    public boolean delete(User user) {

        return this.booleanReturnQueryFunc(
                session -> session.createQuery(
                                "delete from User where id = :id").
                                setParameter("id", user.getId()).
                                executeUpdate(),
                "delete");
    }

    private Boolean booleanReturnQueryFunc(final ToIntFunction<Session> func, String methodName) {
        Boolean result = false;
        final Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            int executeUpdateResult = func.applyAsInt(session);
            transaction.commit();
            if (executeUpdateResult == 1) {
                result = true;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LoggerService.LOGGER.error(
                    "Exception in AccountHibernateDBStore."
                            + methodName
                            + " method", e);
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
            Query<User> query = session.createQuery("from User u order by u.id");
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
        return this.optionalUserReturnQueryFunc(
                session -> {
                    final Query<User> query =
                            session.createQuery(
                                    "from User i where i.id = :fid").
                                    setParameter("fid", id);
                    return query.uniqueResultOptional();
                }, "findById");
    }

    @Override
    public Optional<User> findByLoginAndPassword(User user) {
        return this.optionalUserReturnQueryFunc(
                session -> {
                    final Query<User> query =
                            session.createQuery(
                                    "from User u "
                                            + "where u.login = :flogin "
                                            + "and u.password = :fpassword").
                                    setParameter("flogin", user.getLogin()).
                                    setParameter("fpassword", user.getPassword());
                    return query.uniqueResultOptional();
                }, "findByLoginAndPassword");
    }

    private Optional<User> optionalUserReturnQueryFunc(final Function<Session, Optional<User>> func,
                                                       String methodName) {
        Optional<User> result = Optional.empty();
        final Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            result = func.apply(session);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LoggerService.LOGGER.error(
                    "Exception in AccountHibernateDBStore."
                            + methodName
                            + " method", e);
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