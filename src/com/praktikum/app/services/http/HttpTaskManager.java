package com.praktikum.app.services.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.praktikum.app.models.*;
import com.praktikum.app.services.Managers;
import com.praktikum.app.services.inFileManager.FileBackendTasksManager;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackendTasksManager {
    private static final String TASKS = "tasks";
    private static final String SUB_TASKS = "subtasks";
    private static final String EPICS = "epics";
    private static final String HISTORY = "history";
    private final Gson gson;
    private final KVTaskClient client;

    public HttpTaskManager(String uri) {
        this(uri, false);
    }

    public HttpTaskManager(String uri, boolean isNeedLoad) {
        super(null);
        gson = Managers.buildGsonWithLocalDateTimeAdapter();
        client = new KVTaskClient(uri);
        if(isNeedLoad){
            load();
        }
    }

    private void load() {
        Type tasksType = new TypeToken<List<Task>>(){}.getType();
        List<Task> tasks = gson.fromJson(client.load(TASKS), tasksType);
        if (tasks != null) {
            for (Task task : tasks) {
                int id = task.getId();
                this.tasks.put(id, task);
                this.prioritizedTasks.add(task);
                checkLastIdForCounter(id);
            }
        }

        Type subtasksType = new TypeToken<List<Subtask>>(){}.getType();
        List<Subtask> subtasks = gson.fromJson(client.load(SUB_TASKS), subtasksType);
        if (subtasks != null) {
            for (Subtask subtask: subtasks){
                int id = subtask.getId();
                this.subtasks.put(id, subtask);
                this.prioritizedTasks.add(subtask);
                checkLastIdForCounter(id);
            }
        }

        Type epicsType = new TypeToken<List<Epic>>(){}.getType();
        List<Epic> epics = gson.fromJson(client.load(EPICS), epicsType);
        if (epics != null) {
            for (Epic epic: epics){
                int id = epic.getId();
                this.epics.put(id, epic);
                checkLastIdForCounter(id);
            }
        }

        Type historyType = new TypeToken<List<Task>>(){}.getType();
        List<Task> history = gson.fromJson(client.load(HISTORY), historyType);
        if (history != null) {
            for (Task task : history) {
                getHistory().add(this.findTask(task.getId()));
            }
        }
    }
    private void checkLastIdForCounter(int id){
        if (id > this.id) {
            this.id = id;
        }
    }
    @Override
    protected void save() {
        String jsonTasks = gson.toJson(getTasks());
        client.put(TASKS, jsonTasks);
        String jsonEpics = gson.toJson(getEpics());
        client.put(EPICS, jsonEpics);
        String jsonSubTask = gson.toJson(getSubtasks());
        client.put(SUB_TASKS, jsonSubTask);
        String jsonHistoryView = gson.toJson(getHistory().stream().map(Task::getId).collect(Collectors.toList()));
        client.put(HISTORY, jsonHistoryView);
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