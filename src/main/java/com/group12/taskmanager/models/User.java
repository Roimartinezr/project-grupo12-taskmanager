package com.group12.taskmanager.models;

import com.group12.taskmanager.services.GroupService;
import com.group12.taskmanager.services.GroupUserService;

import java.util.ArrayList;
import java.util.List;

public class User {
    private static int globalID = 0;
    private int id;
    private String nombre;
    private String email;
    private String password;
    private List<Group> groups;

    public User(String nombre, String email, String password) {
        User.globalID++;
        this.id = User.globalID;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.groups = new ArrayList<>();
        Group newGroup = new Group("TASK_"+this.id, this);
        groups.add(newGroup);
        GroupUserService groupUserService = new GroupUserService();
        groupUserService.addEntry(newGroup.getId(), this.id);
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public void updateGroups(Group group) {
        this.groups.add(group);
    }
}
