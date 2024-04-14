import exceptions.ManagerSaveException;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

public class Main {
    public static void main(String[] args) throws ManagerSaveException {
        TaskManager taskManager;
        taskManager = Managers.getDefault();
        Task task = new Task("Задача1", Status.NEW, "описаниеЗадачи1");
        Task task2 = new Task("Задача2", Status.NEW, "описаниеЗадачи1");
        Epic epic = new Epic("Эпик1", Status.NEW, "описаниеЗадачи1");
        Epic epic2 = new Epic("Эпик2", Status.NEW, "описаниеЗадачи1");
        SubTask subTask = new SubTask("Задача1", Status.NEW, "описаниеЗадачи1", epic.getId());
        System.out.println(task.getType());
        System.out.println(epic.getType());
        System.out.println(subTask.getType());
    }
}