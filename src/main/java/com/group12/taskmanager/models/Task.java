package com.group12.taskmanager.models;

public class Task {
    private Long id;
    private String title;
    private String description;
    private Proyecto proyecto; // Relación con un proyecto

    public Task() {}

    public Task(Long id, String title, String description, Proyecto proyecto) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.proyecto = proyecto;
    }

    public Task(String title, String description) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
