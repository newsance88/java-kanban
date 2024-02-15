import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private ArrayList<Task> allTasks = new ArrayList<>();
    private HashMap<Integer,Task> mainTasks = new HashMap<>();
    private HashMap<Integer,Subtask> subTasks = new HashMap<>();
    private HashMap<Integer,Epic> epicTasks = new HashMap<>();

    public Task addTask(Task task) {
        task.id = Task.generalId++;
        mainTasks.put(task.id,task);
        return task;
    }

}
