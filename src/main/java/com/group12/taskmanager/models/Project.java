package com.group12.taskmanager.models;

public class Project {
    private static int globalID = 0;
    private int id;
    private String name;
    private Group group; // Relación con un grupo


    public Project() {}

    public Project(String name, Group group) {
        Project.globalID += 1;
        this.id = Project.globalID;
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
