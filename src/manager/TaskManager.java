package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;

public interface TaskManager {

    ArrayList<Task> getHistory();

    ArrayList<SubTask> getSubsFromEpic(int id);

    void deleteSub(int id);

    SubTask getSub(int id);

    void deleteTask(int id);

    Task getTask(int id);

    void deleteEpic(int id);

    Epic getEpic(int id);

    void removeTasks();

    void removeEpics();

    void removeSubs();

    Task updateTask(Task task);

    Epic updateEpic(Epic epic);

    SubTask updateSub(SubTask subTask);

    Epic addEpic(Epic epic);

    SubTask addSub(SubTask subTask);

    Task addTask(Task task);

    ArrayList<Epic> getAllEpics();

    ArrayList<SubTask> getAllSubs();

    ArrayList<Task> getAllTasks();
}
