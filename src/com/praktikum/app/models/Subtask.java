package com.praktikum.app.models;

import com.praktikum.app.models.utils.Status;
import com.praktikum.app.models.utils.TypeTask;

public class Subtask extends Task{
    private final int epicId;
    public Subtask(String name, String description, Status status, Integer epicId) {
       this.name = name;
       this.description = description;
       this.status = status;
       this.epicId = epicId;
       this.type = TypeTask.SUB_TASK;
    }

    public Subtask(String name, String description, Status status, Integer epicId, int id) {
        super(name, description, status, id);
        this.epicId = epicId;
        this.type = TypeTask.SUB_TASK;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicID=" + epicId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
