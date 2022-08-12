package ru.job4j.todo.exception;

import java.io.Serial;

public class InvalidUserPropertyException extends IllegalArgumentException {

    @Serial
    private static final long serialVersionUID = -5180726958581269155L;

    public InvalidUserPropertyException(String s) {
        super(s);
    }

    public InvalidUserPropertyException(String message, Throwable cause) {
        super(message, cause);
    }
}