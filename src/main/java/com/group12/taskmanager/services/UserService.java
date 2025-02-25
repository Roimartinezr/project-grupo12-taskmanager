package com.group12.taskmanager.services;

import com.group12.taskmanager.models.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final List<User> USERS;

    public UserService() {
        USERS = new ArrayList<>();
    }

    public List<User> getAllUsers() {
        return USERS;
    }
    public void addUser(User user) {
        if (!USERS.contains(user))
            USERS.add(user);
    }

    public User findUserById(int id) {
        for (User user : USERS) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }
}
