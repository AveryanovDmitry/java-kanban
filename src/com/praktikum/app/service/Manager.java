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

    private int idEpic = 1;
    private int idSubTask = 1;
    private int idTask = 1;

    /**
     * Создание задачи
     */
    public void createTask(Task task) {
        task.setId(idTask);
        idTask++;
        tasks.put(task.getId(), task);
    }

    public void createEpicTask(Epic epic) {
        epic.setId(idEpic);
        idEpic++;
        epics.put(epic.getId(), epic);
    }

    public void createSubTask(Subtask task) {
        task.setId(idSubTask);
        subtasks.put(idSubTask, task);
        Epic epic = epics.get(subtasks.get(idSubTask).getEpicID());
        if (epic != null) {
            epic.addToSubTasks(idSubTask);
            updateStatusEpic(epic);
        }
        idSubTask++;
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
    public Set<Subtask> getSubTasksSetByEpicId(int id) {
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
        idTask = 1;
    }

    public void deleteAllEpicTasks() {
        subtasks.clear();
        epics.clear();
        idSubTask = 1;
        idEpic = 1;
    }

    public void deleteAllSubTasks() {
        for (Epic epic : epics.values()) {
            epic.clearSubTasks();
            epic.setStatus(Status.NEW);
        }
        subtasks.clear();
        idSubTask = 1;
    }

    /**
     * удаление по id
     */
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void deleteSubtaskById(int id) {
        int epicId = subtasks.get(id).getEpicID();
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
    public void updateTask(Task task) {//у аргумента не присвоен id, поэтому здесь у меня поиск по имени задачи,
        // id присваивается в менеджере при добавлении в мапу, не в модели
        for (Integer id : tasks.keySet()) {
            if (task.getName().equals(tasks.get(id).getName())) {
                task.setId(id);//Нахожу по имени обновляемую задачу и так определяю её id
                tasks.put(id, task);
            }
        }
    }

    public void updateEpic(Epic newEpic) {//у аргумента не присвоен id, поэтому здесь у меня поиск по имени задачи,
        // id присваивается в менеджере при добавлении в мапу, не в модели
        for (Integer id : epics.keySet()) {
            if (newEpic.getName().equals(epics.get(id).getName())) {
                newEpic.setId(id);//Нахожу по имени обновляемую задачу и так определяю её id
                newEpic.setSubTasks(epics.get(id).getSubTasks());
                epics.put(id, newEpic);
            }
        }
    }

    public void updateSubTask(Subtask subtask) {//у аргумента не присвоен id, поэтому здесь у меня поиск по имени задачи,
        // id присваивается в менеджере при добавлении в мапу, не в модели.
        for (Integer id : subtasks.keySet()) {
            if (subtask.getName().equals(subtasks.get(id).getName())) {
                subtask.setId(id);//Нахожу по имени обновляемую задачу и так определяю её id
                subtasks.put(id, subtask);
                updateStatusEpic(epics.get(subtasks.get(id).getEpicID()));
            }
        }
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
            if (subtasks.get(idSubtask).getStatus() != Status.NEW)
                flagNew = 0;
            if (subtasks.get(idSubtask).getStatus() != Status.DONE)
                flagDone = 0;
            if (flagNew == 0 && flagDone == 0) {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            }
        }
        if (flagDone == 1)
            epic.setStatus(Status.DONE);
        else
            epic.setStatus(Status.NEW);
    }
}
