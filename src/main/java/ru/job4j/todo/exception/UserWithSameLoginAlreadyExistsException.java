package ru.job4j.todo.exception;

import java.io.Serial;

public class UserWithSameLoginAlreadyExistsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6281556187105771115L;

    public UserWithSameLoginAlreadyExistsException(String s) {
        super(s);
    }

    public UserWithSameLoginAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}