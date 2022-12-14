package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.LoggerService;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TaskHibernateDBStore implements TasksStore, AutoCloseable {

    private final SessionFactory sessionFactory;

    public TaskHibernateDBStore(EntityManagerFactory factory) {
        if (factory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("Factory is not a hibernate factory");
        }
        this.sessionFactory = factory.unwrap(SessionFactory.class);
    }

    @Override
    public Task add(Task task) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(task);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LoggerService.LOGGER.error("Exception in TaskHibernateDBStore.add method", e);
        } finally {
            session.close();
        }
        return task;
    }

    @Override
    public boolean update(Task task) {
        boolean result = false;
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            int executeUpdateResult = session.createQuery(
                    "update Task t set "
                            + "t.name = :fname,"
                            + "t.description = :fdescription,"
                            + "t.status = :fstatus "
                            + "where t.id = :fid").
                    setParameter("fname", task.getName()).
                    setParameter("fdescription", task.getDescription()).
                    setParameter("fstatus", task.getStatus()).
                    setParameter("fid", task.getId()).
                    executeUpdate();
            transaction.commit();
            if (executeUpdateResult == 1) {
                result = true;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LoggerService.LOGGER.error("Exception in TaskHibernateDBStore.update method", e);
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public boolean delete(Task task) {
        boolean result = false;
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            int executeUpdateResult = session.createQuery("delete from Task where id = :id")
                    .setParameter("id", task.getId())
                    .executeUpdate();
            transaction.commit();
            if (executeUpdateResult == 1) {
                result = true;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LoggerService.LOGGER.error("Exception in TaskHibernateDBStore.delete method", e);
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public List<Task> findAll() {
        List<Task> result = new ArrayList<>();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            @SuppressWarnings("unchecked")
            Query<Task> query = session.createQuery("from Task t order by t.id asc");
            result.addAll(query.list());
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LoggerService.LOGGER.error("Exception in TaskHibernateDBStore.findAll method", e);
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public Optional<Task> findById(int id) {
        Optional<Task> result = Optional.empty();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Query query =
                    session.createQuery(
                            "from Task i where i.id = :fid").
                            setParameter("fid", id);
            if (query.uniqueResult() != null) {
                result = Optional.of((Task) query.uniqueResult());
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LoggerService.LOGGER.error("Exception in TaskHibernateDBStore.findById method", e);
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public List<Task> findAllWithCertainStatus(int status) {
        List<Task> result = new ArrayList<>();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            @SuppressWarnings("unchecked")
            Query<Task> query = session.createQuery("from Task i where i.status = :fstatus").
                    setParameter("fstatus", status);
            result.addAll(query.list());
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LoggerService.LOGGER.
                    error("Exception in TaskHibernateDBStore.findAllWithCertainStatus method", e);
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(
                new StandardServiceRegistryBuilder().configure().build());
    }
}