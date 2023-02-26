package com.praktikum.app.models;

import com.praktikum.app.models.utils.Status;
import com.praktikum.app.models.utils.TypeTask;

import java.util.Objects;

public class Task {
    protected int id;
    protected String name;
    protected String description;
    protected Status status;

    protected TypeTask type;

    public Task() {
    }

    public Task(String name, String description, Status status, TypeTask type) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = type;
    }

    public Task(String name, String description, Status status, int id) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TypeTask getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task tmp = (Task) obj;
        return Objects.equals(name, tmp.getName()) &&
                Objects.equals(description, tmp.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
