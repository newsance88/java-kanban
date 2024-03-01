import manager.HistoryManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        HistoryManager historyManager = Managers.getDefaultHistory();
        TaskManager taskManager = Managers.getDefault();
        Task task = new Task("Задача1", Status.NEW, "описаниеЗадачи1");
        taskManager.addTask(task);
        taskManager.getTask(task.getId());
        //historyManager.add(task);
        System.out.println(taskManager.getHistory());
    }
}