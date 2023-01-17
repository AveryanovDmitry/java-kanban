package com.praktikum.app.service;

import com.praktikum.app.models.Epic;
import com.praktikum.app.models.Subtask;
import com.praktikum.app.models.Task;

import java.util.*;

public interface TaskManager {
    /**
     * Создание задачи
     */
    void createTask(Task task);

    void createEpicTask(Epic epic);

    void createSubTask(Subtask task);

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    List<Task> getTasks();

    /**
     * получение по id
     */
    Task getTaskById(int id);

    Epic getEpicTaskById(int id);

    Subtask getSubTaskById(int id);

    /**
     * получение множества подзадач эпика по id
     */
    Set<Subtask> getSubTasksByEpicId(int id);

    /**
     * удаление всез задач
     */
    void deleteAllTasks();

    void deleteAllEpicTasks();

    void deleteAllSubTasks();

    /**
     * удаление по id
     */
    void deleteTaskById(int id);

    void deleteSubtaskById(int id);

    void deleteEpicById(int epicId);

    /**
     * обновление задачи
     */
    void updateTask(Task task);

    void updateEpic(Epic newEpic);

    void updateSubtask(Subtask subtask);

    /**
     *  получение истории
     */
    List<Task>getHistory();
}
