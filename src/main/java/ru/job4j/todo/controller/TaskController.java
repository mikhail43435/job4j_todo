package ru.job4j.todo.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;
import ru.job4j.todo.service.TaskStatusHandler;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;

@ThreadSafe
@Controller
public class TaskController {

    private final TaskService taskService;
    private final TaskStatusHandler taskStatusHandler;

    public TaskController(TaskService taskService, TaskStatusHandler taskStatusHandler) {
        this.taskService = taskService;
        this.taskStatusHandler = taskStatusHandler;
    }

    @GetMapping("/tasks")
    public String tasks(Model model, HttpSession session) {
        model.addAttribute("tasks", taskService.findAll());
        model.addAttribute("TaskStatusHandler", taskStatusHandler);
        return "tasks";
    }

    @GetMapping("/showTask/{taskId}")
    public String showTask(Model model,
                           HttpSession session,
                           @PathVariable("taskId") int id) {
        model.addAttribute("task", taskService.findById(id).get());
        model.addAttribute("TaskStatusHandler", taskStatusHandler);
        return "showTask";
    }

    @GetMapping("/createTask")
    public String createTaskGet(Model model,
                                HttpSession session,
                                @ModelAttribute Task task) {
        model.addAttribute("task", task);
        return "createTask";
    }

    @PostMapping("/createTask")
    public String createTaskPost(Model model,
                                 HttpSession session,
                                 @ModelAttribute Task task) {
        task.setStatus(1);
        task.setCreated(LocalDate.now());
        Task newTask = new Task(0, "q", "qq", 1, LocalDate.now());
        taskService.add(newTask);
        return "redirect:/tasks";
    }
}
