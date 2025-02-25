package com.group12.taskmanager.services;

import com.group12.taskmanager.models.Group;
import com.group12.taskmanager.models.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {
    private final List<Group> GROUPS;

    public GroupService() {
        this.GROUPS = new ArrayList<>();
    }

    public List<Group> getAllGroups() {
        return GROUPS;
    }
    public void addGroup(Group group) {
        if (!GROUPS.contains(group))
            GROUPS.add(group);
    }

    public Group findGroupById(int id) {
        for (Group group : GROUPS) {
            if (group.getId() == id) {
                return group;
            }
        }
        return null;
    }
}
