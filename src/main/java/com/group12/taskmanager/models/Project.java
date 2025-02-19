package com.group12.taskmanager.models;

import com.group12.taskmanager.services.TaskService;

import java.util.ArrayList;
import java.util.List;

public class Project {
    private static int globalID = 0;
    private int id;
    private String name;
    private Group group; // Relación con un grupo
    private List<Task> tasks;

    public Project(String name, Group group) {
        Project.globalID += 1;
        this.id = Project.globalID;
        this.name = name;
        this.group = group;
        this.tasks = new ArrayList<>();
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

    public List<Task> getTasks() {
        return tasks != null ? tasks : new ArrayList<>();
    }
    public void addTask(String title, String description) {
        Task newTask = new Task(title, description, this);
        tasks.add(newTask);
        TaskService taskService = new TaskService();
        taskService.addTask(newTask);
    }

}
