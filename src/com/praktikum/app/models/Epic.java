package com.praktikum.app.models;

import com.praktikum.app.models.utils.Status;
import com.praktikum.app.models.utils.TypeTask;

import java.util.HashSet;
import java.util.Set;

public class Epic extends Task{
    private final Set<Integer> subTasks = new HashSet<>();
    public Epic(String name, String description) {
        super(name, description, Status.NEW);
    }
    public Epic(String name, String description, int id) {
        super(name, description, Status.NEW, id);
    }
    public Epic(String name, String description, int id, Status status) {
        super(name, description, status, id);
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

    @Override
    public String toString() {
        return "Epic{" +
                "id= " + this.id +
                " name= " + this.name +
                " description= " + this.description +
                " subTaskList= " + subTasks +
                " status=" + this.status +
                '}';
    }
}
