package com.group12.taskmanager.services;

import com.group12.taskmanager.models.Project;
import com.group12.taskmanager.models.Task;
import com.group12.taskmanager.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task addTask(Task task) {
        return taskRepository.save(task);
    }

    public Task findTaskById(int id) {
        return taskRepository.findById(id).orElse(null);
    }

    public boolean removeTask(int id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Task updateTask(int id, String title, String description, String imagePath) {
        Task task = findTaskById(id);
        if (task != null) {
            if (title != null) task.setTitle(title);
            if (description != null) task.setDescription(description);
            if (imagePath != null) task.setImagePath(imagePath); // solo en memoria
            return taskRepository.save(task);
        }
        return null;
    }

    public List<Task> getProjectTasks(Project project) {
        return taskRepository.findByProject(project);
    }
}
