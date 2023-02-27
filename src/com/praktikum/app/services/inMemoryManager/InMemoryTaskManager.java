package com.praktikum.app.services.inMemoryManager;

import com.praktikum.app.models.Epic;
import com.praktikum.app.models.Subtask;
import com.praktikum.app.models.Task;
import com.praktikum.app.models.utils.Status;
import com.praktikum.app.services.Managers;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final Map<Integer, Task> tasks = new HashMap<>();

    protected int id = 1;

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
    public void addTask(Task task) {
        task.setId(id);
        id++;
        tasks.put(task.getId(), task);
    }
    @Override
    public void addEpicTask(Epic epic) {
        epic.setId(id);
        id++;
        epics.put(epic.getId(), epic);
    }
    @Override
    public void addSubTask(Subtask task) {
        task.setId(id);
        subtasks.put(id, task);
        Epic epic = epics.get(subtasks.get(id).getEpicId());
        if (epic != null) {
            epic.addToSubTasks(id);
            updateStatusEpic(epic);
        }
        id++;
    }
    protected void addSubTaskForLoadFromFile(Subtask task) {
        subtasks.put(id, task);
        Epic epic = epics.get(subtasks.get(id).getEpicId());
        if (epic != null) {
            epic.addToSubTasks(id);
            updateStatusEpic(epic);
        }
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
        deleteFromHistory(tasks.keySet());
        tasks.clear();

        if(epics.isEmpty() && subtasks.isEmpty()){
            id = 1;
        }
    }

    @Override
    public void deleteAllEpicTasks() {
        deleteFromHistory(subtasks.keySet());
        deleteFromHistory(epics.keySet());
        subtasks.clear();
        epics.clear();
        if(tasks.isEmpty()){
            id = 1;
        }
    }

    @Override
    public void deleteAllSubTasks() {
        deleteFromHistory(subtasks.keySet());
        for (Epic epic : epics.values()) {
            epic.clearSubTasks();
            epic.setStatus(Status.NEW);
        }
        subtasks.clear();
    }

    /**
     * удаляем все задачи из истории по ключу
     */
    private void deleteFromHistory(Set<Integer> tasksId){
        if(tasksId == null){
            return;
        }
        for (Integer id: tasksId){
            historyManager.remove(id);
        }
    }

    /**
     * удаление по id
     */
    @Override
    public void deleteTaskById(int id) {
        if(tasks.remove(id) != null){
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if(subtask != null){
            historyManager.remove(id);
            int epicId = subtask.getEpicId();
            Epic epic = epics.get(epicId);
            epic.getSubTasks().remove(id);
            updateStatusEpic(epic);
        }
    }

    @Override
    public void deleteEpicById(int epicId) {
        for (Integer id : epics.get(epicId).getSubTasks()) {
            if(subtasks.remove(id) != null){
                historyManager.remove(id);
            }
        }
        if(epics.remove(epicId) != null){
            historyManager.remove(epicId);
        }
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
    protected void updateStatusEpic(Epic epic) {
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
