package com.group12.taskmanager.controllers;

import com.group12.taskmanager.models.Project;
import com.group12.taskmanager.services.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class ProjectController {

    //1. Mostrar lista de proyectos en project.mustache
    @GetMapping("/")
    public String getProjects(Model model) {
        List<Project> projects = ProjectService.getAllProjects();
        model.addAttribute("projects", projects);
        return "index"; // Renderiza "index.mustache"
    }

    //2. Guardar una nueva tarea desde el formulario
    @PostMapping("/save_project")
    public String saveProject(@RequestParam String name) {
        ProjectService.addProject(new Project(name, null));
        return "redirect:/"; // Redirigir a la página principal
    }

    @GetMapping("/new_project")
    public ResponseEntity<?> newProject() {
        return ResponseEntity.ok(Collections.singletonMap("mensaje", "Abriendo modal"));
    }

    @GetMapping("/project/{id}")
    public String getProjectById(@PathVariable Long id, Model model) {
        Project project = ProjectService.findById(id);

        if (project != null) {
            model.addAttribute("project", project);
            model.addAttribute("tasks", project.getTasks()); // Pasar solo las tareas del proyecto
        } else {
            model.addAttribute("project", null);
            model.addAttribute("tasks", new ArrayList<>()); // Lista vacía si no hay proyecto
        }

        return "project"; // Renderiza project.mustache
    }
}
