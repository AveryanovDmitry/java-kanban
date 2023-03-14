package com.praktikum.app.models;

import com.praktikum.app.models.utils.Status;
import com.praktikum.app.models.utils.TypeTask;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Epic extends Task{
    protected LocalDateTime endTime = LocalDateTime.MAX;
    private final Set<Integer> subTasks = new HashSet<>();
    public Epic(String name, String description) {
        super(name, description, Status.NEW);
    }
    public Epic(String name, String description, int id) {
        super(name, description, Status.NEW, id);
    }
    public Epic(String name, String description, int id, Status status, LocalDateTime startTime, int duration) {
        super(name, description, status, startTime, duration, id);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }
    @Override
    public TypeTask getType(){
        return TypeTask.EPIC;
    }
    public Set<Integer> getSubTasks() {
        return subTasks;
    }

    public void addToSubTasks(Integer id){
        subTasks.add(id);
    }

    public void clearSubTasks(){
        subTasks.clear();
    }

    public void setStartTimeIfEarly(LocalDateTime start){
        if(start.isBefore(startTime)){
            startTime = start;
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id= " + this.id +
                " name= " + this.name +
                " description= " + this.description +
                " subTaskList= " + subTasks +
                " status=" + this.status +
                ", Time Start=" + timeToString(startTime) +
                ", Duration=" + durationToString() +
                ", Time End=" + timeToString(endTime) +
                '}';
    }
}
