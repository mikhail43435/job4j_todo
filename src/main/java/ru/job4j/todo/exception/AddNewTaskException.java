package ru.job4j.todo.exception;

public class AddNewTaskException extends RuntimeException {

    public AddNewTaskException(String s) {
        super(s);
    }

    public AddNewTaskException(String message, Throwable cause) {
        super(message, cause);
    }
}