package ru.job4j.todo.exception;

import java.io.Serial;

public class AddNewTaskException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1649542374540697509L;

    public AddNewTaskException(String s) {
        super(s);
    }

    public AddNewTaskException(String message, Throwable cause) {
        super(message, cause);
    }
}