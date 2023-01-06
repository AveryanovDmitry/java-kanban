package Tasks;

import Tasks.Utils.Status;

import java.util.HashSet;
import java.util.Set;

public class Epic extends Task{
    Set<Integer> subTaskSet = new HashSet<>();
    public Epic(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
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
                "id= " + this.id +
                " name= " + this.name +
                " description= " + this.description +
                " subTaskList= " + subTaskSet +
                " status=" + this.status +
                '}';
    }
}
