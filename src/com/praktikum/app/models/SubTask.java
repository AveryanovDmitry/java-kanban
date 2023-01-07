package com.praktikum.app.models;

import com.praktikum.app.models.utils.Status;

public class SubTask extends Task{

    Integer epicID;
    public SubTask(String name, String description, Status status, Integer epicId) {
       this.name = name;
       this.description = description;
       this.status = status;
       this.epicID = epicId;
    }

    public Integer getEpicID() {
        return epicID;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicID=" + epicID +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
