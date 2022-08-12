package ru.job4j.todo.exception;

import java.io.Serial;

public class UpdateUserException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3894231910204043383L;

    public UpdateUserException(String s) {
        super(s);
    }

    public UpdateUserException(String message, Throwable cause) {
        super(message, cause);
    }
}