package com.group12.taskmanager.models;

public class Proyect {
    private static int globalID = 0;
    private int id;
    private String name;
    private Group group; // Relación con un grupo

    public Proyect() {}

    public Proyect(String name, Group group) {
        Proyect.globalID += 1;
        this.id = Proyect.globalID;
        this.name = name;
        this.group = group;
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

    public Group getGrupo() {
        return group;
    }

    public void setGrupo(Group group) {
        this.group = group;
    }
}
