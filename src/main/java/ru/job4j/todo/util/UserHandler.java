package ru.job4j.todo.util;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.User;

import javax.servlet.http.HttpSession;

@Service
@ThreadSafe
public class UserHandler {

    private UserHandler() {
    }

    public User handleUserOfCurrentSession(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User(-1, "Guest", "", null);
        }
        return user;
    }
}


