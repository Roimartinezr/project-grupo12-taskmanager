package com.group12.taskmanager.services;

import com.group12.taskmanager.models.Group_User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupUserService {
    private final List<Group_User> GROUP_USERS = new ArrayList<>();

    public GroupUserService() {
    }

    public void addEntry(int idGroup, int idUser) {
        GROUP_USERS.add(new Group_User(idGroup, idUser));
    }

    public List<Integer> getGroupUsers(int groupID) {
        List<Integer> list = new ArrayList<>();
        for (Group_User entry : GROUP_USERS) {
            if (groupID == entry.getIdGroup()) {
                list.add(entry.getIdUser());
            }
        }
        return list;
    }

    public List<Integer> getUserGroups(int userID) {
        List<Integer> list = new ArrayList<>();
        for (Group_User entry : GROUP_USERS) {
            if (entry.getIdUser() == userID) {
                list.add(entry.getIdGroup());
            }
        }
        return list;
    }

}
