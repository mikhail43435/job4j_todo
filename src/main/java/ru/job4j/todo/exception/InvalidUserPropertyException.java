package ru.job4j.todo.exception;

public class InvalidUserPropertyException extends IllegalArgumentException {

    public InvalidUserPropertyException(String s) {
        super(s);
    }

    public InvalidUserPropertyException(String message, Throwable cause) {
        super(message, cause);
    }
}