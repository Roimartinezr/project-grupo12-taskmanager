package com.group12.taskmanager.controllers;

import com.group12.taskmanager.models.Group;
import com.group12.taskmanager.models.Project;
import com.group12.taskmanager.models.Task;
import com.group12.taskmanager.models.User;
import com.group12.taskmanager.services.GroupService;
import com.group12.taskmanager.services.ProjectService;
import com.group12.taskmanager.services.TaskService;
import com.group12.taskmanager.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
public class ProjectController {

    @Autowired private ProjectService projectService;
    @Autowired private TaskService taskService;
    @Autowired private UserService userService;
    @Autowired private GroupService groupService;

    @GetMapping("/projects")
    public String getProjects(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) return "redirect:/";

        model.addAttribute("user", currentUser);
        List<Project> projects = new ArrayList<>();
        for (Group group : currentUser.getGroups()) {
            projects.addAll(projectService.getProjectsByGroup(group));
        }

        // Grupos donde el usuario es propietario
        List<Group> ownedGroups = currentUser.getGroups().stream()
                .filter(g -> g.getOwner().getId().equals(currentUser.getId()))
                .toList();
        model.addAttribute("ownedGroups", ownedGroups);
        model.addAttribute("multipleGroups", ownedGroups.size() > 1);
        model.addAttribute("singleGroup", ownedGroups.size() == 1 ? ownedGroups.get(0) : null);
        model.addAttribute("projects", projects);

        return "index";
    }

    @PostMapping("/save_project")
    public String saveProject(@RequestParam String name, @RequestParam int groupId) {
        Group group = groupService.findGroupById(groupId);
        if (group == null) return "redirect:/";

        projectService.createProject(name, group);
        return "redirect:/projects";
    }

    @GetMapping("/new_project")
    public ResponseEntity<?> newProject() {
        return ResponseEntity.ok(Collections.singletonMap("mensaje", "Abriendo modal"));
    }

    @GetMapping("/project/{id}")
    public String getProjectById(@PathVariable int id, Model model, HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/";
        Project project = projectService.findProjectById(id);
        List<Task> tasks = taskService.getProjectTasks(project);

        if (project != null) {
            model.addAttribute("idproject", project.getId());
            model.addAttribute("tasks", tasks);
        } else {
            model.addAttribute("idproject", null);
            model.addAttribute("tasks", new ArrayList<>());
        }
        return "project";
    }

    @PostMapping("/project/{id}/save_task")
    @ResponseBody
    public String saveTask(@PathVariable int id,
                           @RequestParam String title,
                           @RequestParam String description,
                           @RequestParam(required = false) MultipartFile image) {

        String imagePath = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/200px-No_image_available.svg.png";

        if (image != null && !image.isEmpty()) {
            try {
                String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";
                File uploadFolder = new File(uploadDir);
                if (!uploadFolder.exists()) uploadFolder.mkdirs();
                String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
                File destinationFile = new File(uploadDir + fileName);
                image.transferTo(destinationFile);
                imagePath = "/uploads/" + fileName;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Project project = projectService.findProjectById(id);
        Task task = new Task(title, description, project, imagePath);
        taskService.addTask(task);
        return "redirect:/project/" + id;
    }

    @DeleteMapping("/project/{id}/delete_task")
    public ResponseEntity<?> deleteTask(@PathVariable int id, @RequestParam int taskId) {
        Task task = taskService.findTaskById(taskId);
        if (task == null || task.getProject().getId() != id)
            return ResponseEntity.status(404).body(Collections.singletonMap("error", "Tarea no encontrada"));

        taskService.removeTask(taskId);
        return ResponseEntity.ok(Collections.singletonMap("message", "Tarea eliminada correctamente"));
    }

    @PutMapping("/project/{id}/edit_task")
    public ResponseEntity<?> editTask(@PathVariable int id,
                                      @RequestParam int taskId,
                                      @RequestParam String title,
                                      @RequestParam String description,
                                      @RequestParam(required = false) MultipartFile image,
                                      @RequestParam(required = false) String imagePath) {

        Task task = taskService.findTaskById(taskId);
        if (task == null || task.getProject().getId() != id)
            return ResponseEntity.status(404).body(Collections.singletonMap("error", "Tarea no encontrada"));

        String newImagePath = imagePath;
        if (image != null && !image.isEmpty()) {
            try {
                String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";
                File uploadFolder = new File(uploadDir);
                if (!uploadFolder.exists()) uploadFolder.mkdirs();
                String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
                File destinationFile = new File(uploadDir + fileName);
                image.transferTo(destinationFile);
                newImagePath = "/uploads/" + fileName;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        taskService.updateTask(taskId, title, description, newImagePath);
        return ResponseEntity.ok(Collections.singletonMap("message", "Tarea actualizada correctamente"));
    }

    @PostMapping("/project/{id}/delete_project")
    public ResponseEntity<?> deleteProject(@PathVariable int id) {
        Project project = projectService.findProjectById(id);
        if (project == null)
            return ResponseEntity.status(404).body(Collections.singletonMap("error", "Proyecto no encontrado"));

        projectService.deleteProject(id);
        boolean removed = projectService.findProjectById(id) == null;
        return removed
                ? ResponseEntity.ok(Collections.singletonMap("message", "Proyecto eliminado correctamente"))
                : ResponseEntity.status(500).body(Collections.singletonMap("error", "Error al eliminar el proyecto"));
    }

    @PutMapping("/project/{id}/edit_project")
    public ResponseEntity<?> editProject(@PathVariable int id, @RequestParam String name) {
        Project project = projectService.findProjectById(id);
        if (project == null)
            return ResponseEntity.status(404).body(Collections.singletonMap("error", "Proyecto no encontrado"));

        project.setName(name);
        projectService.updateProject(project);
        return ResponseEntity.ok(Collections.singletonMap("success", true));
    }
}
