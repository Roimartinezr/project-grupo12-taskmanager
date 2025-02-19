package com.group12.taskmanager.services;

import com.group12.taskmanager.models.Project;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {
    private static List<Project> projects = new ArrayList<>(); // Simulación de base de datos

    public static List<Project> getAllProjects() {
        return projects;
    }

    public static void addProject(Project project) {
        projects.add(project);
    }

    public static Project findById(Long id) {
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
