package com.group12.taskmanager.models;

public class Proyect {
    private Long id;
    private String nombre;
    private Group group; // Relación con un grupo

    public Proyect() {}

    public Proyect(Long id, String nombre, Group group) {
        this.id = id;
        this.nombre = nombre;
        this.group = group;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Group getGrupo() {
        return group;
    }

    public void setGrupo(Group group) {
        this.group = group;
    }
}
