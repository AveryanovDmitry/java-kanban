package com.praktikum.app.models;

import com.praktikum.app.models.utils.Status;
import com.praktikum.app.models.utils.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Task {
    protected int id;
    protected String name;
    protected String description;
    protected Status status;
    protected int duration;
    protected LocalDateTime startTime;

    public Task() {
    }

    public Task(String name, String description, Status status, LocalDateTime startTime, int duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String name, String description, Status status, LocalDateTime startTime, int duration, int id) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        this.id = id;
    }

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description, Status status, int id) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
    }

    public LocalDateTime getEndTime() {
        if (startTime == null) {
            return null;
        }
        return startTime.plus(Duration.ofMinutes(duration));
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public TypeTask getType() {
        return TypeTask.TASK;
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

    public String timeToString(LocalDateTime time) {
        if (time == null) {
            return "Время не указано";
        }
        return time.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }

    public String durationToString() {
        if ((getDuration() == 0)) {
            return "Продолжительность не указана";
        }
        return duration + " минут";
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", Time Start=" + timeToString(startTime) +
                ", Duration=" + durationToString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Task task = (Task) o;

        return name.equals(task.getName())
                && description.equals(task.getDescription())
                && status == task.getStatus()
                && duration == task.getDuration()
                && equalsStartTime(task.getStartTime());
    }

    protected boolean equalsStartTime(LocalDateTime checkTime) {
        if (startTime == null && checkTime == null) {
            return true;
        } else if (startTime == null || checkTime == null) {
            return false;
        } else if (Math.abs(startTime.getMinute() - checkTime.getMinute()) <= 1 || Math.abs(startTime.getMinute() - checkTime.getMinute()) == 59) {
            return startTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH")).equals(
                    checkTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH")));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 71;
        if (name != null) {
            hash = hash + name.hashCode();
        }
        hash = hash * 31;
        if (description != null) {
            hash = hash + description.hashCode();
        }
        if (status != null) {
            hash = hash + status.hashCode();
        }
        hash = hash + duration;
        if (startTime != null) {
            hash = hash + startTime.hashCode();
        }
        return Math.abs(hash);
    }
}
