package ru.job4j.todo.store;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

@Repository
public class AccountHibernateStoreSafe<T extends User>
        extends AccountHibernateDBStoreAbstract<T>  implements AccountStore<T> {

    public AccountHibernateStoreSafe(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
