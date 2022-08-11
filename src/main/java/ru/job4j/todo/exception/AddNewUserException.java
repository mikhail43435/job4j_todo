package ru.job4j.todo.exception;

public class AddNewUserException extends RuntimeException {

    public AddNewUserException(String s) {
        super(s);
    }

    public AddNewUserException(String message, Throwable cause) {
        super(message, cause);
    }
}