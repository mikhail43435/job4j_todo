package ru.job4j.todo.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.job4j.todo.exception.UserWithSameLoginAlreadyExistsException;
import ru.job4j.todo.model.User;
import ru.job4j.todo.model.UserWithoutPassword;
import ru.job4j.todo.service.AccountService;
import ru.job4j.todo.util.UserHandler;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@ThreadSafe
@Controller
public class UserController {

    private final AccountService accountService;
    private final UserHandler userHandler;

    public UserController(AccountService accountService,
                          UserHandler userHandler) {
        this.accountService = accountService;
        this.userHandler = userHandler;
    }

    @GetMapping("/loginUser")
    public String loginUserGet(Model model,
                               HttpSession session,
                               @RequestParam(name = "fail", required = false) Boolean fail) {
        model.addAttribute("fail", fail != null);
        model.addAttribute("user", userHandler.handleUserOfCurrentSession(session));
        return "loginUser";
    }

    @PostMapping("loginUser")
    public String loginUserPost(Model model,
                                HttpSession session,
                                @ModelAttribute User user) {

        Optional<UserWithoutPassword> userFound =
                (Optional<UserWithoutPassword>) accountService.findByLoginAndPassword(user);
        if (!userFound.isPresent()) {
            return "redirect:/loginUser?fail=true";
        }
        session.setAttribute("user", userFound.get());
        return "redirect:/tasks";
    }

    @GetMapping("/logoutUser")
    public String logoutUser(Model model,
                         HttpSession session) {
        session.invalidate();
        return "redirect:/tasks";
    }

    @GetMapping("registrationUser")
    public String registrationUserGet(Model model,
                                      HttpSession session,
                                      @RequestParam(name = "fail", required = false) Boolean fail,
                                      @RequestParam(name = "errorMessage",
                                              defaultValue = "") String errorMessage) {

        model.addAttribute("fail", fail != null);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("user", userHandler.handleUserOfCurrentSession(session));
        return "registrationUser";
    }

    @PostMapping("registrationUser")
    public String registrationUserPost(Model model,
                                       HttpSession session,
                                       @ModelAttribute User user,
                                       RedirectAttributes redirectAttributes) {
        String redirectString = "redirect:/registrationUser?fail=true";
        try {
            accountService.add(user);
        } catch (UserWithSameLoginAlreadyExistsException | IllegalArgumentException e) {
            redirectAttributes.addAttribute(
                    "errorMessage",
                    "An error occurred while creating a new user. " + e.getMessage());
            return redirectString;
        } catch (Exception e) {
            redirectAttributes.addAttribute(
                    "errorMessage",
                    "An database error occurred. " + e.getMessage());
            return redirectString;
        }
        session.setAttribute("user", user);
        model.addAttribute("user", userHandler.handleUserOfCurrentSession(session));
        return "registrationUser";
    }

/*    @ExceptionHandler({Exception.class})
    public String handleException(Exception e, Model model) {
        LoggerService.LOGGER.error("Exception in AccountService.java", e);
        return "redirect:/error";
    }*/
}