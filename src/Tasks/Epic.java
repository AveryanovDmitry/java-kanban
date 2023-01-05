package Tasks;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Epic extends Task{
    Set<Integer> subTaskSet = new HashSet<>();
    public Epic(int id, String name, String description, Integer[] task) {
        this.id = id;
        this.name = name;
        this.description = description;
        subTaskSet.addAll(Arrays.asList(task));
    }

    public Set<Integer> getSubTaskSet() {
        return subTaskSet;
    }

    public void addToSubTaskList(Integer id){
        subTaskSet.add(id);
    }

    public void clearSubTaskSet(){
        subTaskSet.clear();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTaskList=" + subTaskSet +
                '}';
    }
}
