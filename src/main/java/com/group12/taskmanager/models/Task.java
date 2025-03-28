package com.group12.taskmanager.models;

import jakarta.persistence.*;

@Entity
@Table(name = "`TASK`")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "TITLE", length = 50)
    private String title;

    @Column(name = "DESCRIPTION", length = 700)
    private String description;

    @ManyToOne
    @JoinColumn(name = "PROJECT", nullable = false)
    private Project project;

    @Transient
    private String imagePath; // no está en la BBDD

    public Task() {}

    public Task(String title, String description, Project project, String imagePath) {
        this.title = title;
        this.description = description;
        this.project = project;
        this.imagePath = imagePath;
    }

    // Getters y setters

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Project getProject() {
        return project;
    }
    public void setProject(Project project) {
        this.project = project;
    }

    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
