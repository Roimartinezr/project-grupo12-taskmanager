package com.group12.taskmanager.models;

public class Group_User {
    private int idGroup;
    private int idUser;

    public Group_User(int idGroup, int idUser) {
        this.idGroup = idGroup;
        this.idUser = idUser;
    }

    public int getIdUser() {
        return idUser;
    }
    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdGroup() {
        return idGroup;
    }
    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }
}
