package com.group12.taskmanager.models;

import com.group12.taskmanager.services.GroupUserService;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private static int globalID = 0;
    private int id;
    private String nombre;
    private List<User> users;
    private GroupUserService groupUserService = new GroupUserService();

    public Group(String nombre, User firstUser) {
        Group.globalID++;
        this.id = globalID;
        this.nombre = nombre;
        users = new ArrayList<>();
        users.add(firstUser);

        groupUserService.addEntry(this.id, firstUser.getId());
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

    public List<User> getUsers() {
        return users;
    }
    public void addUser(User user) {
        users.add(user);
        groupUserService.addEntry(this.id, user.getId()); // Actualizar tabla relación
        user.updateGroups(this); // Actualizar registro de grupos del usuario
    }

}
