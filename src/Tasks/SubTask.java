package Tasks;

import Tasks.Utils.Status;

public class SubTask extends Task{

    Integer epicID;
    public SubTask(String name, String description, Status statu, Integer epicId) {
       this.name = name;
       this.description = description;
       this.status = status;
    }

    public Integer getEpicID() {
        return epicID;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicID=" + epicID +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
