package ru.job4j.todo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum TaskStatus {
    NEW("New task"),
    FINISHED("Finished task");

    private final String description;

    TaskStatus(String description) {
        this.description = description;
    }

    public static List<TaskStatus> asList() {
        List<TaskStatus> result = new ArrayList<>();
        Collections.addAll(result, values());
        return result;
    }

    public String getDescription() {
        return description;
    }
}