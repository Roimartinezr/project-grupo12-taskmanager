package com.group12.taskmanager.models;

import com.group12.taskmanager.services.GroupUserService;

import java.util.List;

public class Group {
    private static int globalID = 0;
    private int id;
    private String name;
    private List<User> users;
    private final GroupUserService GROUPUSER_SERVICE = new GroupUserService();

    public Group(String name, User firstUser) {
        Group.globalID++;
        this.id = globalID;
        this.name = name;
        this.users = GROUPUSER_SERVICE.getGroupUsers(this.id);
        users.add(firstUser);
        GROUPUSER_SERVICE.addEntry(this, firstUser);
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }
    public void addUser(User user) {
        users.add(user);
        GROUPUSER_SERVICE.addEntry(this, user); // Actualizar tabla relación
        user.updateGroups(this); // Actualizar registro de grupos del usuario
    }

}
