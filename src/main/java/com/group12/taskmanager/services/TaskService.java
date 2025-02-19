package com.group12.taskmanager.services;

import com.group12.taskmanager.models.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    private List<Task> tasks; // Simulación de base de datos

    public TaskService() {
        tasks =  new ArrayList<>();
    }

    public List<Task> getAllTasks() {
        return tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }
}

