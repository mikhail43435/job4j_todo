package ru.job4j.todo.store;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

@Repository
public class AccountHibernateStore<T extends User>
        extends AccountHibernateDBStoreAbstract<T> implements AccountStore<T> {

    public AccountHibernateStore(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
