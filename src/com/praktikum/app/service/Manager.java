package com.praktikum.app.service;

import com.praktikum.app.models.Epic;
import com.praktikum.app.models.Subtask;
import com.praktikum.app.models.Task;
import com.praktikum.app.models.utils.Status;

import java.util.*;

public class Manager {
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();
    private Map<Integer, Task> tasks = new HashMap<>();

    private int id = 1;

    /**
     * Создание задачи
     */
    public void createTask(Task task) {
        task.setId(id);
        id++;
        tasks.put(task.getId(), task);
    }

    public void createEpicTask(Epic epic) {
        epic.setId(id);
        id++;
        epics.put(epic.getId(), epic);
    }

    public void createSubTask(Subtask task) {
        task.setId(id);
        subtasks.put(id, task);
        Epic epic = epics.get(subtasks.get(id).getEpicId());
        if (epic != null) {
            epic.addToSubTasks(id);
            updateStatusEpic(epic);
        }
        id++;
    }

    public List<Epic> getEpics() {
        return List.copyOf(epics.values());
    }

    public List<Subtask> getSubtasks() {
        return List.copyOf(subtasks.values());
    }

    public List<Task> getTasks() {
        return List.copyOf(tasks.values());
    }

    /**
     * получение по id
     */
    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicTaskById(int id) {
        return epics.get(id);
    }

    public Subtask getSubTaskById(int id) {
        return subtasks.get(id);
    }

    /**
     * получение множества подзадач эпика по id
     */
    public Set<Subtask> getSubTasksByEpicId(int id) {
        Set<Integer> subtasksId = epics.get(id).getSubTasks();
        Set<Subtask> result = new HashSet<>();
        for (int idSubtask : subtasksId) {
            result.add(subtasks.get(idSubtask));
        }
        return result;
    }

    /**
     * удаление всез задач
     */
    public void deleteAllTasks() {
        tasks.clear();
        if(epics.isEmpty() && subtasks.isEmpty()){
            id = 1;
        }
    }

    public void deleteAllEpicTasks() {
        subtasks.clear();
        epics.clear();
        if(tasks.isEmpty()){
            id = 1;
        }
    }

    public void deleteAllSubTasks() {
        for (Epic epic : epics.values()) {
            epic.clearSubTasks();
            epic.setStatus(Status.NEW);
        }
        subtasks.clear();
    }

    /**
     * удаление по id
     */
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void deleteSubtaskById(int id) {
        int epicId = subtasks.get(id).getEpicId();
        subtasks.remove(id);
        Epic epic = epics.get(epicId);
        epic.getSubTasks().remove(id);
        updateStatusEpic(epic);
    }


    public void deleteEpicById(int epicId) {
        for (Integer id : epics.get(epicId).getSubTasks()) {
            subtasks.remove(id);
        }
        epics.remove(epicId);
    }

    /**
     * обновление задачи
     */
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic newEpic) {
        epics.put(newEpic.getId(), newEpic);
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        updateStatusEpic(epics.get(subtask.getEpicId()));
    }

    /**
     * метод обновления статуса для эпика
     */
    private void updateStatusEpic(Epic epic) {
        if (epic.getSubTasks().isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        int flagDone = 1;
        int flagNew = 1;
        for (Integer idSubtask : epic.getSubTasks()) {
            Status status = subtasks.get(idSubtask).getStatus();
            if (status != Status.NEW)
                flagNew = 0;
            if (status != Status.DONE)
                flagDone = 0;
            if (flagNew == 0 && flagDone == 0) {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            }
        }
        if (flagDone == 1) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.NEW);
        }
    }
}
