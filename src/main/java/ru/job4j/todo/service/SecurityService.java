package ru.job4j.todo.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.User;

import java.util.Arrays;

@Service
@ThreadSafe
public class SecurityService {

    public void wipeUserPassword(User user) {
        char[] password = user.getPassword();
        Arrays.fill(password, ' ');
    }
}