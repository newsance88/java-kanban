import exceptions.ManagerSaveException;
import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws ManagerSaveException {
        InMemoryTaskManager taskManager = Managers.getDefault();
        Task task1 = new Task("Задача1", Status.NEW, "описаниеЗадачи1", Duration.ofMinutes(10), LocalDateTime.now());
        Epic epic = new Epic("Задача2", Status.NEW, "описаниеЗадачи1");
        SubTask subTask = new SubTask("Задача3", Status.NEW, "описаниеЗадачи1", 2, Duration.ofMinutes(5),LocalDateTime.of(20000,2,2,2,2));
        SubTask subTask2 = new SubTask("Задача4", Status.NEW, "описаниеЗадачи1", 2, Duration.ofMinutes(5),LocalDateTime.of(33000,2,2,2,2));
        taskManager.addTask(task1);
        taskManager.addEpic(epic);
        taskManager.addSub(subTask);
        taskManager.addSub(subTask2);

    }
}