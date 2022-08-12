package ru.job4j.todo.exception;

import java.io.Serial;

public class AddNewUserException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -534712827855306892L;

    public AddNewUserException(String s) {
        super(s);
    }

    public AddNewUserException(String message, Throwable cause) {
        super(message, cause);
    }
}