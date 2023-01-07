package com.praktikum.app.service;

import com.praktikum.app.models.Epic;
import com.praktikum.app.models.SubTask;
import com.praktikum.app.models.Task;

import java.util.*;

public class Manager {
    private Map<Integer, Epic> epicTasksMap = new HashMap<>();
    private Map<Integer, SubTask> subTasksMap = new HashMap<>();
    private Map<Integer, Task> tasksMap = new HashMap<>();

    /**
     * Создание задачи
     */
    public void createTask(Task task) {
        task.setId(tasksMap.size() + 1);
        tasksMap.put(task.getId(), task);
    }

    public void createEpicTask(Epic epic) {
        epic.setId(epicTasksMap.size() + 1);
        epicTasksMap.put(epic.getId(), epic);
    }

    public void createSubTask(SubTask task) {
        int subTaskID = subTasksMap.size() + 1;
        task.setId(subTaskID);
        subTasksMap.put(subTaskID, task);
        Epic epic = epicTasksMap.get(subTasksMap.get(subTaskID).getEpicID());
        if (epic != null) {
            epic.addToSubTaskList(subTaskID);
        }
    }

    public Collection<Epic> getEpicTasksMap() {
        return epicTasksMap.values();
    }

    public Collection<SubTask> getSubTasksMap() {
        return subTasksMap.values();
    }

    public Collection<Task> getTasksMap() {
        return tasksMap.values();
    }

    /**
     * получение по id
     */
    public Task getTaskById(int id) {
        return tasksMap.get(id);
    }

    public Epic getEpicTaskById(int id) {
        return epicTasksMap.get(id);
    }

    public SubTask getSubTaskById(int id) {
        return subTasksMap.get(id);
    }

    /**
     * получение множества подзадач эпика по id
     */
    public Set<SubTask> getSubTasksSetByEpicID(int id) {
        Set<Integer> subTasksID = epicTasksMap.get(id).getSubTaskSet();
        Set<SubTask> result = new HashSet<>();
        for (int i : subTasksID) {
            result.add(subTasksMap.get(i));
        }
        return result;
    }

    /**
     * удаление всез задач
     */
    public void deleteAllTasks() {
        tasksMap.clear();
    }

    public void deleteAllEpicTasks() {
        for (Integer i : epicTasksMap.keySet()) {
            subTasksMap.remove(i);
        }
        epicTasksMap.clear();
    }

    public void deleteAllSubTasks() {
        for (Epic epic : epicTasksMap.values()) {
            epic.clearSubTaskSet();
        }
        subTasksMap.clear();
    }

    /**
     * удаление по id
     */
    public void deleteTaskById(int id) {
        tasksMap.remove(id);
    }

    public void deleteSubtaskById(int id) {
        int epicId = subTasksMap.get(id).getEpicID();
        subTasksMap.remove(id);
        epicTasksMap.get(epicId).getSubTaskSet().remove(id);
    }


    public void deleteEpicById(int epicId) {
        for (Integer id : epicTasksMap.get(epicId).getSubTaskSet()) {
            deleteSubtaskById(id);
        }
        epicTasksMap.remove(epicId);
    }

    /**
     * обновление задачи
     */
    public void updateTask(Task task) {
        for (Integer id : tasksMap.keySet()) {
            if (task.getName().equals(tasksMap.get(id).getName())) {
                task.setId(id);
                tasksMap.put(id, task);
            }
        }
    }

    public void updateEpic(Epic epic) {
        for (Integer id : epicTasksMap.keySet()) {
            if (epic.getName().equals(epicTasksMap.get(id).getName())) {
                epic.setId(id);
                epicTasksMap.put(id, epic);
            }
        }
    }

    public void updateSubTask(SubTask subTask) {
        for (Integer id : subTasksMap.keySet()) {
            if (subTask.getName().equals(subTasksMap.get(id).getName())) {
                subTask.setId(id);
                subTasksMap.put(id, subTask);
            }
        }
    }
}
