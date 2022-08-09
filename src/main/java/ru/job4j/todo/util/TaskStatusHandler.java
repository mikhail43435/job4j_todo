package ru.job4j.todo.util;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;

@Service
@ThreadSafe
public class TaskStatusHandler {

    public String getStatusString(int statusId) {
        switch (statusId) {
            case 1:
                return "New";
            case 2:
                return "Finished";
            default:
                return "unknown status";
        }
    }
}