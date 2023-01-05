package Tasks;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Epic extends Task{
    Set<SubTask> subTaskList = new HashSet<>();
    public Epic(int id, String name, String description, SubTask[] task) {
        this.id = id;
        this.name = name;
        this.description = description;
        subTaskList.addAll(Arrays.asList(task));
    }

    public Set<SubTask> getSubTaskList() {
        return subTaskList;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTaskList=" + subTaskList +
                '}';
    }
}
