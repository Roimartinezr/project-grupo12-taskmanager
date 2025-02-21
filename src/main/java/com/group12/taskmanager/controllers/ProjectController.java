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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Controller
public class ProjectController {
    private final ProjectService projectService;
    private final TaskService taskService;

    // Test Data
    public ProjectController() {
        this.projectService = new ProjectService();
        this.taskService = new TaskService();
        for (int i = 1; i <= 5; i++) {
            Project newProject = new Project("Proyecto"+i, null);
            projectService.addProject(newProject);
            for (int j = 0; j < 10; j++) {
                taskService.addTask(new Task("tarea"+j, "Esto es un ejemplo"+i+j, newProject.getId()));
            }
        }
    }

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
    public String saveTask(
            @PathVariable int id,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam(required = false) MultipartFile image) {

        String imagePath = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/200px-No_image_available.svg.png"; // Imagen de prueba

        if (image != null && !image.isEmpty()) {
            try {
                // Ruta de almacenamiento
                String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";
                File uploadFolder = new File(uploadDir);
                if (!uploadFolder.exists()) {
                    uploadFolder.mkdirs(); // Crea la carpeta si no existe
                }

                // Generar un nombre de archivo único
                String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
                File destinationFile = new File(uploadDir + fileName);

                // Guardar el archivo en la carpeta de uploads
                image.transferTo(destinationFile);

                // Ruta accesible desde el navegador
                imagePath = "/uploads/" + fileName;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Task newTask = new Task(title, description, id, imagePath);
        taskService.addTask(newTask);

        return "redirect:/project/" + id;
    }

    @DeleteMapping("/project/{id}/delete_task")
    public ResponseEntity<?> deleteTask(@PathVariable int id, @RequestParam int taskId) {
        taskService.removeTask(taskId);
        boolean removed = taskService.findTaskById(taskId) == null;

        if (removed) {
            return ResponseEntity.ok(Collections.singletonMap("message", "Tarea eliminada correctamente"));
        } else {
            return ResponseEntity.status(404).body(Collections.singletonMap("error", "Tarea no encontrada"));
        }
    }

    @PutMapping("/project/{id}/edit_task")
    public ResponseEntity<?> editTask(
            @PathVariable int id,
            @RequestParam int taskId,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String imagePath) {

        Task task = taskService.findTaskById(taskId);
        if (task == null) {
            return ResponseEntity.status(404).body(Collections.singletonMap("error", "Tarea no encontrada"));
        }

        taskService.updateTask(taskId, title, description, imagePath);
        return ResponseEntity.ok(Collections.singletonMap("message", "Tarea actualizada correctamente"));
    }

}
