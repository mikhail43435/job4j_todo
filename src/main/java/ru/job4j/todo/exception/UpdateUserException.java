package ru.job4j.todo.exception;

public class UpdateUserException extends RuntimeException {

    public UpdateUserException(String s) {
        super(s);
    }

    public UpdateUserException(String message, Throwable cause) {
        super(message, cause);
    }
}