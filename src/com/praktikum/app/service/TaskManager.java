package com.praktikum.app.service;

import com.praktikum.app.models.Epic;
import com.praktikum.app.models.Subtask;
import com.praktikum.app.models.Task;

import java.util.*;

public interface TaskManager {
    /**
     * Создание задачи
     */
    public void createTask(Task task);

    public void createEpicTask(Epic epic);

    public void createSubTask(Subtask task);

    public List<Epic> getEpics();

    public List<Subtask> getSubtasks();

    public List<Task> getTasks();

    /**
     * получение по id
     */
    public Task getTaskById(int id);

    public Epic getEpicTaskById(int id);

    public Subtask getSubTaskById(int id);

    /**
     * получение множества подзадач эпика по id
     */
    public Set<Subtask> getSubTasksByEpicId(int id);

    /**
     * удаление всез задач
     */
    public void deleteAllTasks();

    public void deleteAllEpicTasks();

    public void deleteAllSubTasks();

    /**
     * удаление по id
     */
    public void deleteTaskById(int id);

    public void deleteSubtaskById(int id);

    public void deleteEpicById(int epicId);

    /**
     * обновление задачи
     */
    public void updateTask(Task task);

    public void updateEpic(Epic newEpic);

    public void updateSubtask(Subtask subtask);

    /**
     *  получение истории
     */
    List<Task>getHistory();
}
