package ru.job4j.todo.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.exception.AddNewUserException;
import ru.job4j.todo.exception.InvalidUserPropertyException;
import ru.job4j.todo.exception.UpdateUserException;
import ru.job4j.todo.exception.UserWithSameLoginAlreadyExistsException;
import ru.job4j.todo.model.User;
import ru.job4j.todo.store.AccountHibernateDBStore;

import java.util.List;
import java.util.Optional;

@ThreadSafe
@Service
public class AccountService<T extends User> {

    private final AccountHibernateDBStore<T> store;
    private final ValidationService validationService;

    public AccountService(AccountHibernateDBStore<T> store,
                          ValidationService validationService) {
        this.store = store;
        this.validationService = validationService;
    }

    public T add(T user) {

        Optional<InvalidUserPropertyException> userFieldValidationResult = validateUserFields(user);
        if (userFieldValidationResult.isPresent()) {
            throw userFieldValidationResult.get();
        }

        try {
            store.add(user);
        } catch (UserWithSameLoginAlreadyExistsException e) {
            throw new AddNewUserException("Error in DB occurred while adding new user. "
                    + e.getMessage());
        } catch (Exception e) {
            throw new AddNewUserException("Error in DB occurred while adding new user.");
        }
        return user;
    }

    private Optional<InvalidUserPropertyException> validateUserFields(T user) {
        Optional<InvalidUserPropertyException> validationException;

        validationException = validationService.validateUserName(user.getName());
        if (validationException.isPresent()) {
            return validationException;
        }
        validationException = validationService.validateUserLogin(user.getLogin());
        if (validationException.isPresent()) {
            return validationException;
        }
        validationException = validationService.validateUserPassword(user.getPassword());
        if (validationException.isPresent()) {
            return validationException;
        }
        return Optional.empty();
    }

    public boolean update(T user) {
        if (!store.findByLoginAndPassword(user).isPresent()) {
            throw new UpdateUserException("Error occurred while updating user data. "
                    + "User with specified login/password not found.");
        }
        return store.update(user);
    }

    public boolean delete(T user) {
        return store.delete(user);
    }

    public List<T> findAll() {
        return store.findAll();
    }

    public Optional<T> findById(int id) {
        return store.findById(id);
    }

    public Optional<T> findByLoginAndPassword(T user) {
        return store.findByLoginAndPassword(user);
    }

    public void close() throws Exception {
        store.close();
    }
}