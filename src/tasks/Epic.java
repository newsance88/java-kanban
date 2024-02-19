package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Integer> subTaskId = new ArrayList<>();
    public Epic(String name, Status status) {
        super(name, status);
    }
    public Epic(String name, Status status, int id) {
        super(name, status);
        this.id = id;
    }
}
