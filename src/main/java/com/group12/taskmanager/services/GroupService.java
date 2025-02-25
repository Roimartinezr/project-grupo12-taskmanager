package com.group12.taskmanager.services;

import com.group12.taskmanager.models.Group;
import com.group12.taskmanager.models.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {
    private List<Group> groups;
    private final UserService USERSERVICE;

    public GroupService() {
        this.groups = new ArrayList<>();
        this.USERSERVICE = new UserService();
    }

    public List<Group> getGroups() {}
}
