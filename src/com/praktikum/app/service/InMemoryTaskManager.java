package com.praktikum.app.service;

import com.praktikum.app.models.Epic;
import com.praktikum.app.models.Subtask;
import com.praktikum.app.models.Task;
import com.praktikum.app.models.utils.Status;

import java.util.*;

public class InMemoryTaskManager implements TaskManager{

    public HistoryManager historyManager = Managers.getDefaultHistory();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();
    private Map<Integer, Task> tasks = new HashMap<>();

    private int id = 1;

    /**
     * получение истории
     */
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    /**
     * Создание задачи
     */
    @Override
    public void createTask(Task task) {
        task.setId(id);
        id++;
        tasks.put(task.getId(), task);
    }
    @Override
    public void createEpicTask(Epic epic) {
        epic.setId(id);
        id++;
        epics.put(epic.getId(), epic);
    }
    @Override
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
    @Override
    public List<Epic> getEpics() {
        return List.copyOf(epics.values());
    }
    @Override
    public List<Subtask> getSubtasks() {
        return List.copyOf(subtasks.values());
    }
    @Override
    public List<Task> getTasks() {
        return List.copyOf(tasks.values());
    }

    /**
     * получение по id
     */
    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }
    @Override
    public Epic getEpicTaskById(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }
    @Override
    public Subtask getSubTaskById(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    /**
     * получение множества подзадач эпика по id
     */
    @Override
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
    @Override
    public void deleteAllTasks() {
        tasks.clear();
        if(epics.isEmpty() && subtasks.isEmpty()){
            id = 1;
        }
    }

    @Override
    public void deleteAllEpicTasks() {
        subtasks.clear();
        epics.clear();
        if(tasks.isEmpty()){
            id = 1;
        }
    }

    @Override
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
    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteSubtaskById(int id) {
        int epicId = subtasks.get(id).getEpicId();
        subtasks.remove(id);
        Epic epic = epics.get(epicId);
        epic.getSubTasks().remove(id);
        updateStatusEpic(epic);
    }


    @Override
    public void deleteEpicById(int epicId) {
        for (Integer id : epics.get(epicId).getSubTasks()) {
            subtasks.remove(id);
        }
        epics.remove(epicId);
    }

    /**
     * обновление задачи
     */
    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic newEpic) {
        epics.put(newEpic.getId(), newEpic);
    }
    @Override
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
