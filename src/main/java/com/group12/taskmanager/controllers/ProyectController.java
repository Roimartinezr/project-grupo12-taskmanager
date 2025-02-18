package com.group12.taskmanager.controllers;

import com.group12.taskmanager.models.Group;
import com.group12.taskmanager.models.Proyect;
import com.group12.taskmanager.services.ProyectService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
public class ProyectController {
    private final ProyectService proyectService;

    public ProyectController(ProyectService proyectService) {
        this.proyectService = proyectService;
    }

    //1. Mostrar lista de proyectos en proyect.mustache
    @GetMapping("/")
    public String getProyects(Model model) {
        List<Proyect> proyects = proyectService.getAllProyects();
        model.addAttribute("proyects", proyects);
        return "index"; // Renderiza "index.mustache"
    }

    //2. Guardar una nueva tarea desde el formulario
    @PostMapping("/save_proyect")
    public String saveProyect(@RequestParam String name) {
        proyectService.addProyect(new Proyect(name, null));
        return "redirect:/"; // Redirigir a la página principal
    }

    @GetMapping("/new_proyect")
    public ResponseEntity<?> newProyect() {
        return ResponseEntity.ok(Collections.singletonMap("mensaje", "Abriendo modal"));
    }
}
