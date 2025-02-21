package com.group12.taskmanager.services;

import com.group12.taskmanager.models.Project;
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

    public void removeTask(int id) {
        Task rmtask = findTaskById(id);
        tasks.remove(rmtask);
        System.out.println("Removed task: " + rmtask);
    }

    public Task findTaskById(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }

    public List<Task> getProjectTasks(Project project) {
        List<Task> projectTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getProjectID() == project.getId()) {
                projectTasks.add(task);
            }
        }
        return projectTasks;
    }

}

