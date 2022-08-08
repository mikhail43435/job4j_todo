package ru.job4j.todo.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
                                @RequestParam(name = "fail", required = false) Boolean fail,
                                @RequestParam(name = "errorMessage", defaultValue = "") String errorMessage,
                                @ModelAttribute Task task) {
        model.addAttribute("fail", fail != null);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("task", task);
        return "createTask";
    }

    @PostMapping("/createTask")
    public String createTaskPost(Model model,
                                 HttpSession session,
                                 @ModelAttribute Task task,
                                 RedirectAttributes redirectAttributes) {
        task.setStatus(1);
        task.setCreated(LocalDate.now());
        try {
            taskService.add(task);
        } catch (Exception e) {
            redirectAttributes.addAttribute("errorMessage", e.getMessage());
            return "redirect:/createTask?fail=true";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/markTask/{taskId}/{status}")
    public String markTask(Model model,
                           HttpSession session,
                           @PathVariable("taskId") int id,
                           @PathVariable("status") int status) {
        Task task = taskService.findById(id).get();
        task.setStatus(status);
        taskService.update(task);
        model.addAttribute("task", task);
        model.addAttribute("TaskStatusHandler", taskStatusHandler);
        return "showTask";
    }

    @GetMapping("/editTask/{taskId}")
    public String editTask(Model model,
                           HttpSession session,
                           @PathVariable("taskId") int id) {
        Task task = taskService.findById(id).get();
        model.addAttribute("task", task);
        model.addAttribute("statusString", taskStatusHandler.getStatusString(task.getStatus()));
        return "editTask";
    }

    @PostMapping("/updateTask")
    public String updateTask(Model model,
                             HttpSession session,
                             @ModelAttribute Task task) {
        taskService.update(task);
        return "redirect:/showTask/" + task.getId();
    }

    @GetMapping("/deleteTask/{taskId}")
    public String deleteTask(Model model,
                             HttpSession session,
                             @PathVariable("taskId") int id) {
        taskService.delete(taskService.findById(id).get());
        return "redirect:/tasks/";
    }
}