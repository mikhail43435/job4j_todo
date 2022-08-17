package ru.job4j.todo.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.TaskStatus;
import ru.job4j.todo.service.LoggerService;
import ru.job4j.todo.service.TaskService;
import ru.job4j.todo.util.UserHandler;

import javax.servlet.http.HttpSession;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@ThreadSafe
@Controller
public class TaskController {

    private final TaskService taskService;
    private final UserHandler userHandler;

    public TaskController(TaskService taskService,
                          UserHandler userHandler) {
        this.taskService = taskService;
        this.userHandler = userHandler;
    }

    @GetMapping("/index")
    public String index(Model model,
                        HttpSession session) {
        return "redirect:/tasks";
    }

    @GetMapping("/tasks")
    public String tasks(Model model,
                        HttpSession session,
                        @RequestParam(
                                name = "mode",
                                defaultValue = "0",
                                required = false) String mode) {
        int modeIntValue;

        try {
            modeIntValue = Integer.parseInt(mode);
        } catch (Exception e) {
            modeIntValue = 0;
        }
        model.addAttribute("tasks", getListOfTasksWithCertainMode(modeIntValue));
        model.addAttribute("HeaderText", getHeader(modeIntValue));
        model.addAttribute("user", userHandler.handleUserOfCurrentSession(session));
        return "tasks";
    }

    private List<Task> getListOfTasksWithCertainMode(int mode) {
        List<Task> result;
        if (mode == 1) {
            result = taskService.findAllWithCertainStatus(1);
        } else if (mode == 2) {
            result = taskService.findAllWithCertainStatus(2);
        } else {
            result = taskService.findAll();
        }
        return result;
    }

    private String getHeader(int mode) {
        if (mode == 1) {
            return "New tasks";
        } else if (mode == 2) {
            return "Finished tasks";
        } else {
            return "All tasks";
        }
    }

    @GetMapping("/showTask/{taskId}")
    public String showTask(Model model,
                           HttpSession session,
                           @PathVariable("taskId") int id) {
        model.addAttribute("task", taskService.findById(id).get());
        model.addAttribute("TaskStatusFINISHED", TaskStatus.FINISHED);
        model.addAttribute("TaskStatusNEW", TaskStatus.NEW);
        model.addAttribute("user", userHandler.handleUserOfCurrentSession(session));
        return "showTask";
    }

    @GetMapping("/createTask")
    public String createTaskGet(Model model,
                                HttpSession session,
                                @RequestParam(name = "fail", required = false) Boolean fail,
                                @RequestParam(name = "errorMessage",
                                        defaultValue = "") String errorMessage,
                                @ModelAttribute Task task) {
        model.addAttribute("fail", fail != null);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("task", task);
        model.addAttribute("user", userHandler.handleUserOfCurrentSession(session));
        return "createTask";
    }

    @PostMapping("/createTask")
    public String createTaskPost(Model model,
                                 HttpSession session,
                                 @ModelAttribute Task task,
                                 RedirectAttributes redirectAttributes) {
        task.setStatus(TaskStatus.NEW);
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
                           @PathVariable("status") TaskStatus status) {
        Task task = taskService.findById(id).get();
        task.setStatus(status);
        taskService.update(task);
        model.addAttribute("task", task);
        model.addAttribute("TaskStatusFINISHED", TaskStatus.FINISHED);
        model.addAttribute("TaskStatusNEW", TaskStatus.NEW);
        model.addAttribute("user", userHandler.handleUserOfCurrentSession(session));
        return "showTask";
    }

    @GetMapping("/editTask/{taskId}")
    public String editTask(Model model,
                           HttpSession session,
                           @PathVariable("taskId") int id) {
        Task task = taskService.findById(id).get();
        model.addAttribute("task", task);
        model.addAttribute("statusString", task.getDescription());
        model.addAttribute("user", userHandler.handleUserOfCurrentSession(session));
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
        model.addAttribute("user", userHandler.handleUserOfCurrentSession(session));
        return "redirect:/tasks/";
    }

    @ExceptionHandler({Exception.class})
    public String handleException(Exception e, Model model) {
        LoggerService.LOGGER.error("Exception in TaskController.java", e);
        return "redirect:/error";
    }
}