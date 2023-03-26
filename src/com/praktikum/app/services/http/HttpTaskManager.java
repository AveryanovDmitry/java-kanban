package com.praktikum.app.services.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.praktikum.app.models.*;
import com.praktikum.app.services.Managers;
import com.praktikum.app.services.inFileManager.FileBackendTasksManager;
import com.praktikum.app.services.inMemoryManager.InHistoryTaskManager;

import java.lang.reflect.Type;
import java.util.List;

public class HttpTaskManager extends FileBackendTasksManager {
    private final Gson gson;
    private final KVTaskClient client;
    InHistoryTaskManager inHistoryTaskManager;

    public HttpTaskManager(String uri) {
        super(null);
        this.gson = Managers.buildGsonWithLocalDateTimeAdapter();
        this.client = new KVTaskClient(uri);
        this.inHistoryTaskManager = new InHistoryTaskManager();
    }

    public void load() {
        Type tasksType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> tasks = gson.fromJson(client.load("tasks"), tasksType);
        if (tasks != null) {
            for (Task task : tasks) {
                int id = task.getId();
                this.tasks.put(id, task);
                this.prioritizedTasks.add(task);
                if (id > this.id) {
                    this.id = id;
                }
            }
        }

        Type subtasksType = new TypeToken<List<Subtask>>() {
        }.getType();
        List<Subtask> subtasks = gson.fromJson(client.load("subtasks"), subtasksType);
        if (subtasks != null) {
            for (Subtask subtask: subtasks){
                int id = subtask.getId();
                this.subtasks.put(id, subtask);
                this.prioritizedTasks.add(subtask);
                if (id > this.id) {
                    this.id = id;
                }
            }
        }

        Type epicsType = new TypeToken<List<Epic>>(){}.getType();
        List<Epic> epics = gson.fromJson(client.load("epics"), epicsType);
        if (epics != null) {
            for (Epic epic: epics){
                int id = epic.getId();
                this.epics.put(id, epic);
                this.prioritizedTasks.add(epic);
                if (id > this.id) {
                    this.id = id;
                }
            }
        }

        Type historyType = new TypeToken<List<Task>>(){}.getType();
        List<Task> history = gson.fromJson(client.load("history"), historyType);
        if (history != null) {
            for (Task task : history) {
                inHistoryTaskManager.add(this.findTask(task.getId()));
            }
        }
    }

    @Override
    public void save() {
        String jsonTasks = gson.toJson(getTasks());
        client.put("tasks", jsonTasks);
        String jsonEpics = gson.toJson(getEpics());
        client.put("epics", jsonEpics);
        String jsonSubTask = gson.toJson(getSubtasks());
        client.put("subtasks", jsonSubTask);
        String jsonHistoryView = gson.toJson(getHistory());
        client.put("history", jsonHistoryView);
    }

    protected Task findTask(Integer id) {
        if (tasks.get(id) != null) {
            return tasks.get(id);
        }
        if (epics.get(id) != null) {
            return epics.get(id);
        }
        return subtasks.get(id);
    }
}