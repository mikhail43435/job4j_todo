package ru.job4j.todo.exception;

public class UserWithSameLoginAlreadyExistsException extends RuntimeException {

    public UserWithSameLoginAlreadyExistsException(String s) {
        super(s);
    }

    public UserWithSameLoginAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}