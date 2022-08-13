package ru.job4j.todo.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;

@Entity
public class UserWithoutPassword extends User implements Serializable {

    public UserWithoutPassword() {
    }

    public UserWithoutPassword(int id, String name, String login, char[] password) {
        super(id, name, login, new char[0]);
    }

    @Override
    public String toString() {
        return "UserWithoutPassword {id=" + super.getId()
                + ", name='" + super.getName() + '\''
                + ", login='" + super.getLogin() + '\''
                + ", password=" + Arrays.toString(super.getPassword()) + '\''
                + '}';
    }
}