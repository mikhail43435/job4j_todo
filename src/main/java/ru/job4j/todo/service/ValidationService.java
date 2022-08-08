package ru.job4j.todo.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;

@Service
@ThreadSafe
public class ValidationService {

    public String validateTaskName(String taskName) {
        if (taskName.isBlank()) {
            return "Invalid task name: <"
                    + taskName
                    + "> The length of the task's name can not contain spaces only.";
        } else if (taskName.length() < 1 || taskName.length() > 255) {
            return  "Invalid task name: <"
                    + taskName
                    + "> The length of the task's name must be in the range 1 - 255 .";
        }
        return "";
    }
}
