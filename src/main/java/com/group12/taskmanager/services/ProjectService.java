package com.group12.taskmanager.services;

import com.group12.taskmanager.models.Group;
import com.group12.taskmanager.models.Project;
import com.group12.taskmanager.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project addProject(Project project) {
        return projectRepository.save(project);
    }

    public Project createProject(String name, Group group) {
        Project project = new Project(name, group);
        return projectRepository.save(project);
    }

    public Project findProjectById(int id) {
        return projectRepository.findById(id).orElse(null);
    }

    public boolean deleteProject(int id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void updateProject(Project project) {
        projectRepository.save(project);
    }

    public boolean updateProjectName(int id, String newName) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            project.setName(newName);
            projectRepository.save(project);
            return true;
        }
        return false;
    }

    public List<Project> getProjectsByGroup(Group group) {
        return projectRepository.findByGroup(group);
    }
}
