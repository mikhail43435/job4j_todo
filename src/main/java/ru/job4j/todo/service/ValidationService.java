package ru.job4j.todo.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;

@Service
@ThreadSafe
public class ValidationService {

    public String validateTaskName(String taskName) {
        if (taskName.isBlank()) {
            return "Invalid task name: <"
                    + taskName
                    + "> The task's name cannot contain spaces only.";
        } else if (taskName.length() < 1 || taskName.length() > 255) {
            return "Invalid task name: <"
                    + taskName
                    + "> The length of the task's name must be in the range 1 - 255 .";
        }
        return "";
    }

    public String validateUserName(String userName) {
        if (userName.isBlank()) {
            return "Invalid user name: <"
                    + userName
                    + "> The user's name can not contain spaces only.";
        } else if (userName.length() < 1 || userName.length() > 100) {
            return "Invalid user name: <"
                    + userName
                    + "> The length of the user's name must be in the range 1 - 255 .";
        }
        return "";
    }

    public String validateUserLogin(String userLogin) {
        if (userLogin.isBlank()) {
            return "Invalid login: <"
                    + userLogin
                    + "> The login can not contain spaces only.";
        } else if (userLogin.length() < 1 || userLogin.length() > 100) {
            return "Invalid user name: <"
                    + userLogin
                    + "> The length login must be in the range 1 - 255 .";
        }
        return "";
    }

    public String validateUserPassword(char[] userPassword) {
        if (userPassword.length < 1 || userPassword.length > 100) {
            return "Invalid password length.The password length must be in 1-100 range.";
        }
        for (char ch : userPassword) {
            if (!(Character.isLetter(ch) || Character.isDigit(ch))) {
                return "Invalid password. Password must contain only digits and letters.";
            }
        }
        return "";
    }
}