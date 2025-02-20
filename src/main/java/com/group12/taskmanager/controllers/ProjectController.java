package com.group12.taskmanager.controllers;

import com.group12.taskmanager.models.Project;
import com.group12.taskmanager.models.Task;
import com.group12.taskmanager.services.ProjectService;
import com.group12.taskmanager.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class ProjectController {
    private final ProjectService projectService;
    private final TaskService taskService;

    public ProjectController() {
        this.projectService = new ProjectService();
        this.taskService = new TaskService();
    }

    // Cambiar la ruta de "/" a "/projects"
    @GetMapping("/projects")
    public String getProjects(Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/"; // Si no está autenticado, redirigir al login
        }
        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects);
        return "index"; // Renderiza "index.mustache"
    }

    @PostMapping("/save_project")
    public String saveProject(@RequestParam String name) {
        projectService.addProject(new Project(name, null));
        return "redirect:/projects"; // Redirigir a la página principal
    }

    @GetMapping("/new_project")
    public ResponseEntity<?> newProject() {
        return ResponseEntity.ok(Collections.singletonMap("mensaje", "Abriendo modal"));
    }

    @GetMapping("/project/{id}")
    public String getProjectById(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/"; // Si no está autenticado, redirigir al login
        }
        Project project = projectService.findById(id);
        List<Task> tasks = taskService.getProjectTasks(project);
        if (project != null) {
            model.addAttribute("idproject", project.getId());
            model.addAttribute("tasks", tasks);
        } else {
            model.addAttribute("idproject", null);
            model.addAttribute("tasks", new ArrayList<>());
        }
        return "project"; // Renderiza project.mustache
    }

    @PostMapping("/project/{id}/save_task")
    @ResponseBody
    public String saveTask(@PathVariable int id, @RequestParam String title, @RequestParam String description) {
        Task newTask = new Task(title, description, id);
        taskService.addTask(newTask);
        return "redirect:/project/" + id;
    }
}
