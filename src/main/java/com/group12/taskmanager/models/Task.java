package com.group12.taskmanager.models;

public class Task {
    private static int globalID = 0;
    private int id;
    private String title;
    private String description;
    private Proyecto proyecto; // Relación con un proyecto

    public Task() {}

    public Task(String title, String description, Proyecto proyecto) {
        Task.globalID = Task.globalID + 1;
        this.id = Task.globalID;
        this.title = title;
        this.description = description;
        this.proyecto = proyecto;
    }

    public Task(String title, String description) {
        Task.globalID = Task.globalID + 1;
        this.id = Task.globalID;
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }
}
