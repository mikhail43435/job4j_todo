package ru.job4j.todo.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.exception.InvalidUserPropertyException;

import java.util.Optional;

@Service
@ThreadSafe
public class ValidationService {

    public Optional<IllegalArgumentException> validateTaskName(String taskName) {
        Optional<IllegalArgumentException> result = Optional.empty();
        if (taskName.isBlank()) {
            result = Optional.of(new IllegalArgumentException("Invalid task name: <"
                    + taskName
                    + "> The task's name cannot be blank."));
        } else if (taskName.length() < 1 || taskName.length() > 255) {
            result = Optional.of(new IllegalArgumentException("Invalid task name: <"
                    + taskName
                    + "> The length of the task's name must be in the range 1 - 255 ."));
        }
        return result;
    }

    public Optional<InvalidUserPropertyException> validateUserName(String userName) {
        Optional<InvalidUserPropertyException> result = Optional.empty();
        if (userName.isBlank()) {
            return Optional.of(new InvalidUserPropertyException("Invalid user name: <"
                    + userName
                    + "> The user's name can not contain spaces only."));
        } else if (userName.length() < 1 || userName.length() > 100) {
            return Optional.of(new InvalidUserPropertyException("Invalid user name: <"
                    + userName
                    + "> The length of the user's name must be in the range 1 - 255 ."));
        }
        return result;
    }

    public Optional<InvalidUserPropertyException> validateUserLogin(String userLogin) {
        Optional<InvalidUserPropertyException> result = Optional.empty();
        if (userLogin.isBlank()) {
            result = Optional.of(new InvalidUserPropertyException("Invalid login: <"
                    + userLogin
                    + "> The login can not contain spaces only."));
        } else if (userLogin.length() < 1 || userLogin.length() > 100) {
            result = Optional.of(new InvalidUserPropertyException("Invalid user name: <"
                    + userLogin
                    + "> The length login must be in the range 1 - 255 ."));
        }
        return result;
    }

    public Optional<InvalidUserPropertyException> validateUserPassword(char[] userPassword) {
        Optional<InvalidUserPropertyException> result = Optional.empty();
        if (userPassword.length < 1 || userPassword.length > 100) {
            return Optional.of(new InvalidUserPropertyException("Invalid password length.The "
                    + "password length must be in 1-100 range."));
        }
        for (char ch : userPassword) {
            if (!(Character.isLetter(ch) || Character.isDigit(ch))) {
                result = Optional.of(new InvalidUserPropertyException("Invalid password. "
                        + "Password must contain only digits and letters."));
                break;
            }
        }
        return result;
    }
}