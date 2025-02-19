package com.group12.taskmanager.services;

import com.group12.taskmanager.models.Project;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {
    private List<Project> projects = new ArrayList<>(); // Simulación de base de datos

    public List<Project> getAllProjects() {
        return projects;
    }

    public void addProject(Project project) {
        projects.add(project);
    }

    public Project findById(Long id) {
        if (! projects.isEmpty()) {
            for (Project project : projects) {
                if (project.getId() == id) {
                    return project;
                }
            }
        }
        return null;
    }

}
