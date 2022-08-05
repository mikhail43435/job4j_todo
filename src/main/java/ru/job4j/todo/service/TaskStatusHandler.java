package ru.job4j.todo.service;

import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@ThreadSafe
public class TaskStatusHandler {

    public static String getStatusString(int statusId) {
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