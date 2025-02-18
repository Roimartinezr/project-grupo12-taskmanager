package com.group12.taskmanager.models;

public class Proyecto {
    private Long id;
    private String nombre;
    private Grupo grupo; // Relación con un grupo

    public Proyecto() {}

    public Proyecto(Long id, String nombre, Grupo grupo) {
        this.id = id;
        this.nombre = nombre;
        this.grupo = grupo;
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

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }
}
