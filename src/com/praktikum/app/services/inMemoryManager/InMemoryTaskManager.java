package com.praktikum.app.services.inMemoryManager;

import com.praktikum.app.exceptions.ManagerSaveException;
import com.praktikum.app.models.Epic;
import com.praktikum.app.models.Subtask;
import com.praktikum.app.models.Task;
import com.praktikum.app.models.utils.Status;
import com.praktikum.app.services.Managers;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Set<Task> prioritizedTasks = new TreeSet<>((task1, task2) -> {
        LocalDateTime t1 = task1.getStartTime();
        LocalDateTime t2 = task2.getStartTime();
        if ((t1 != null) && (t2 != null)) {
            return t1.compareTo(t2);
        } else if (t1 == null && t2 != null) {
            return 1;
        } else if (t1 != null) {
            return -1;
        }
        return task1.getId() - task2.getId();
    });

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
        addPrioritizedTask(task);
    }

    @Override
    public void addEpicTask(Epic epic) {
        epic.setId(id);
        id++;
        epics.put(epic.getId(), epic);
        updateTimeEpic(epic);
    }

    @Override
    public void addSubTask(Subtask task) {
        Epic epic = epics.get(task.getEpicId());
        if (epic != null) {
            task.setId(id);
            subtasks.put(id, task);
            epic.addToSubTasks(id);
            updateStatusEpic(epic);
            addPrioritizedTask(task);
            if (task.getStartTime() != null) {
                updateTimeEpic(epic);
            }
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
     * удаление всех задач
     */
    @Override
    public void deleteAllTasks() {
        deleteFromHistory(tasks.keySet());
        prioritizedTasks.removeAll(tasks.values());
        tasks.clear();
        if (epics.isEmpty() && subtasks.isEmpty()) {
            id = 1;
        }
    }

    @Override
    public void deleteAllEpicTasks() {
        deleteFromHistory(subtasks.keySet());
        deleteFromHistory(epics.keySet());
        prioritizedTasks.removeAll(subtasks.values());
        subtasks.clear();
        epics.clear();
        if (tasks.isEmpty()) {
            id = 1;
        }
    }

    @Override
    public void deleteAllSubTasks() {
        prioritizedTasks.removeAll(subtasks.values());
        deleteFromHistory(subtasks.keySet());
        for (Epic epic : epics.values()) {
            epic.clearSubTasks();
            epic.setStatus(Status.NEW);
            updateTimeEpic(epic);
        }
        subtasks.clear();
    }

    /**
     * удаляем все задачи из истории по ключу
     */
    private void deleteFromHistory(Set<Integer> tasksId) {
        for (Integer id : tasksId) {
            historyManager.remove(id);
        }
    }

    /**
     * удаление по id
     */
    @Override
    public void deleteTaskById(int id) {
        Task task = tasks.remove(id);
        if (task != null) {
            historyManager.remove(id);
            prioritizedTasks.remove(task);
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            prioritizedTasks.remove(subtask);
            historyManager.remove(id);
            int epicId = subtask.getEpicId();
            Epic epic = epics.get(epicId);
            epic.getSubTasks().remove(id);
            updateStatusEpic(epic);
            updateTimeEpic(epic);
        }
    }

    @Override
    public void deleteEpicById(int epicId) {
        for (Integer id : epics.get(epicId).getSubTasks()) {
            Subtask subtask = subtasks.remove(id);
            if (subtask != null) {
                prioritizedTasks.remove(subtask);
                historyManager.remove(id);
            }
        }
        Epic epic = epics.remove(epicId);
        if (epic != null) {
            prioritizedTasks.remove(epic);
            historyManager.remove(epicId);
        }
    }

    /**
     * обновление задачи
     */
    @Override
    public void updateTask(Task task) {
        prioritizedTasks.remove(tasks.get(task.getId()));
        tasks.put(task.getId(), task);
        addPrioritizedTask(tasks.get(task.getId()));
    }

    @Override
    public void updateEpic(Epic newEpic) {
        updateTimeEpic(newEpic);
        epics.put(newEpic.getId(), newEpic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());

        prioritizedTasks.remove(subtasks.get(subtask.getId()));
        subtasks.put(subtask.getId(), subtask);
        updateStatusEpic(epic);
        addPrioritizedTask(subtask);
        updateTimeEpic(epics.get(subtask.getEpicId()));
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

    /**
     * Получение списка задач в порядке приоритета.
     */
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    /**
     * Добавление задач в список в порядке приоритета.
     */
    protected void addPrioritizedTask(Task task) {
        if (task.getStartTime() != null) {
            checkTaskPriority(task);
        }
        prioritizedTasks.add(task);
    }

    /**
     * Изменение времени эпика.
     */
    protected void updateTimeEpic(Epic epic) {
        Set<Subtask> subT = getSubTasksByEpicId(epic.getId());
        if (!subT.isEmpty()) {
            LocalDateTime startTime = null;
            LocalDateTime endTime = null;
            int duration = 0;
            for (Subtask subtask : subT) {
                if (subtask.getStartTime() != null) {
                    if (startTime == null || subtask.getStartTime().isBefore(startTime)) {
                        startTime = subtask.getStartTime();
                    } else if (endTime == null || subtask.getEndTime().isAfter(endTime)) {
                        endTime = subtask.getEndTime();
                    }
                }
                duration += subtask.getDuration();
            }
            epic.setStartTime(startTime);
            epic.setEndTime(endTime);
            epic.setDuration(duration);
        }
    }

    /**
     * Проверка времени.
     */
    private boolean doesTimeOverlap(Task task) {
        for (Task check : prioritizedTasks) {
            if (check.getStartTime() == null) {
                break;
            }
            if (!task.getStartTime().isBefore(check.getEndTime()) || !task.getEndTime().isAfter(check.getStartTime())) {
                continue;
            }
            return true;
        }
        return false;
    }

    /**
     * Проверка задач на пересечение.
     */
    private void checkTaskPriority(Task task) {
        if (task.getStartTime() != null && doesTimeOverlap(task)) {
            throw new ManagerSaveException(
                    "Новая задача c id " + task.getId() + " пересекается с текущими задачами, пожалуйста, измените время в одной из задач");
        }
    }
}
