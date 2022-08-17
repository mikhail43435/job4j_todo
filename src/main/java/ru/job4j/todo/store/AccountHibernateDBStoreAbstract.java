package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.exception.UserWithSameLoginAlreadyExistsException;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.LoggerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AccountHibernateDBStoreAbstract<T extends User> {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sessionFactory;

    public AccountHibernateDBStoreAbstract(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void showGenericType() {
        Class<T> springGenericType = (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(),
                AccountStore.class);
        System.out.print("=============== AccountHibernateDBStore generic type >>> ");
        System.out.println(springGenericType);

    }

    public T add(T user) {
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

    /**
     * Update only NAME and PASSWORD fields after verification that such user exists
     */
    public boolean update(T user) {
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

    public boolean delete(T user) {
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

    public List<T> findAll() {
        List<T> result = new ArrayList<>();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Query query = session.createQuery("from User u order by u.id");
            /*result.addAll(query.list());*/
            for (Object o : query.list()) {
                result.add((T) o);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LoggerService.LOGGER.error("Exception in AccountHibernateDBStore.findAll method", e);
        } finally {
            session.close();
        }
        showGenericType();
        System.out.println("==================== findAll method ====================");
        System.out.println(result.toString());
        return result;
    }

    public Optional<T> findById(int id) {
        Optional<T> result = Optional.empty();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Query query =
                    session.createQuery(
                            "from User i where i.id = :fid").
                            setParameter("fid", id);
            if (query.uniqueResult() != null) {
                result = Optional.of((T) query.uniqueResult());
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

    public Optional<T> findByLoginAndPassword(T user) {
        Optional<T> result = Optional.empty();
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
                result = Optional.of((T) query.uniqueResult());
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

    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}