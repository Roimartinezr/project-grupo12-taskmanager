package com.group12.taskmanager.services;

import com.group12.taskmanager.models.Proyect;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProyectService {
    private List<Proyect> proyects = new ArrayList<>(); // Simulación de base de datos

    public List<Proyect> getAllProyects() {
        return proyects;
    }

    public void addProyect(Proyect proyect) {
        proyects.add(proyect);
    }
}
