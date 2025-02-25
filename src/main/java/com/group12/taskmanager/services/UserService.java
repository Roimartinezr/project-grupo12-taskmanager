package com.group12.taskmanager.services;

import com.group12.taskmanager.models.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private List<User> users;

    public UserService() {
        users = new ArrayList<>();
    }

    public List<User> getAllUsers() {
        return users;
    }
}
