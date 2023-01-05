import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.util.*;

public class Manager {
    private Map<Integer, Epic> epicTasksMap = new HashMap<>();
    private Map<Integer, SubTask> subTasksMap = new HashMap<>();
    private Map<Integer, Task> tasksMap = new HashMap<>();

    /**
     * Методы добавления нового объекта в мапы
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

    /**
     * Гетеры всех значений мапы
     */
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
     * Гетеры по id
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
     * Получение множества подзадач эпика по его id
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
     * Методы удаления всех задач
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
     * Методы удаления по id
     */
    public void deleteTaskById(int id) {
        tasksMap.remove(id);
    }

    public void deleteSubtaskById(int id) {
        int epicId = subTasksMap.get(id).getEpicID();
        subTasksMap.remove(id);
        epicTasksMap.get(epicId).getSubTaskSet().remove(id);
    }


    public void deleteEpicById(int id) {
        epicTasksMap.remove(id);
        for (Map.Entry<Integer, SubTask> entry : subTasksMap.entrySet()) {
            if (entry.getValue().getEpicID() == id)
                subTasksMap.remove(entry.getKey());
        }
    }

    /**
     * Методы обновления объекта в мапе
     */
    public void updateTask(Task task) {
        for (Integer id : tasksMap.keySet()) {
            if (task.equals(tasksMap.get(id))) {
                task.setId(id);
                tasksMap.put(id, task);
            }
        }
    }

    public void updateEpic(Epic epic) {
        for (Integer id : epicTasksMap.keySet()) {
            if (epic.equals(epicTasksMap.get(id))) {
                epic.setId(id);
                epicTasksMap.put(id, epic);
            }
        }
    }

    public void updateSubTask(SubTask subTask) {
        for (Integer id : subTasksMap.keySet()) {
            if (subTask.equals(subTasksMap.get(id))) {
                subTask.setId(id);
                subTasksMap.put(id, subTask);
            }
        }
    }
}
