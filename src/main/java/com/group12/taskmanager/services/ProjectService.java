package com.group12.taskmanager.services;

import com.group12.taskmanager.models.Group;
import com.group12.taskmanager.models.Project;
import com.group12.taskmanager.models.Task;
import com.group12.taskmanager.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskService taskService;

    public void createProject(String name, Group group) {
        Project project = new Project(name, group);
        projectRepository.save(project);
    }

    public Project findProjectById(int id) {
        return projectRepository.findById(id).orElse(null);
    }

    public void deleteProject(int projectId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        // eliminar todas las tareas del proyecto
        if (project != null) {
            List<Task> tasks = taskService.getProjectTasks(project);
            for (Task task : tasks) {
                taskService.removeTask(task.getId());
            }
            projectRepository.delete(project);
        }
    }

    public void updateProject(Project project) {
        projectRepository.save(project);
    }

    public List<Project> getProjectsByGroup(Group group) {
        return projectRepository.findByGroup(group);
    }
}
