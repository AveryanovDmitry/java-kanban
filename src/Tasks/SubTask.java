package Tasks;

import Tasks.Utils.Status;

public class SubTask extends Task{

    String epicName;
    public SubTask(int id, String name, String description, Status status) {
        super(id, name, description, status);
    }
}
