package com.praktikum.app.models;

import com.praktikum.app.models.utils.Status;
import com.praktikum.app.models.utils.TypeTask;

import java.time.LocalDateTime;

public class Subtask extends Task{
    private final int epicId;
    public Subtask(String name, String description, Status status, Integer epicId, LocalDateTime startTime, int duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.epicId = epicId;
        this.startTime = startTime;
        this.duration = duration;
    }
    public Subtask(String name, String description, Status status, Integer epicId) {
       this.name = name;
       this.description = description;
       this.status = status;
       this.epicId = epicId;
    }

    public Subtask(String name, String description, Status status, Integer epicId, int id) {
        super(name, description, status, id);
        this.epicId = epicId;
    }
    public Subtask(String name, String description, Status status, Integer epicId, int id, LocalDateTime startTime, int duration) {
        super(name, description, status, startTime, duration, id);
        this.epicId = epicId;
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
                ", Time Start=" + timeToString(startTime) +
                ", Duration=" + durationToString() +
                '}';
    }

    @Override
    public TypeTask getType(){
        return TypeTask.SUB_TASK;
    }
}
