import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import Tasks.Utils.Status;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Manager {
    Map<Integer, Task> taskMap = new HashMap<>();
    Map<Integer, SubTask> subTuskMap = new HashMap<>();
    Map<Integer, Epic> epicMap = new HashMap<>();

    public Task getTaskById(int id) {
        return taskMap.get(id);
    }

    public boolean deleteTaskById(int id) {
        if(taskMap.containsKey(id)){
            taskMap.remove(id);
            return true;
        } else
            return false;
    }

    public Collection<Task> getAllTasks(){
        return epicMap.values();

        https://javarush.com/groups/posts/2004-teorija-dzhenerikov-v-java-ili-gde-na-praktike-stavitjh-skobki
    }

    public void deleteAllTasks(){
        taskMap.clear();
    }

    public SubTask createSubTask(String name, String description) {
        SubTask tmp = new SubTask(subTuskMap.size() + 1, name, description, Status.NEW);
        subTuskMap.put(tmp.getId(), tmp);
        return tmp;
    }

    public Epic createEpic(String name, String description, SubTask... task) {
        Epic tmp = new Epic(epicMap.size() + 1, name, description, task);
        epicMap.put(tmp.getId(), tmp);
        return tmp;
    }

    public void updateTask(Task task, Status status){
        task.setStatus(status);
        if (task.getClass() == Epic.class) {
            Epic task1 = (Epic) task;
            epicMap.put(task.getId(), task1);
        }
    }
}
