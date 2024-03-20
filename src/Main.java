import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

public class Main { //Здесь проверил, все работает
    public static void main(String[] args) {
        TaskManager taskManager;
        taskManager = Managers.getDefault();
        Task task = new Task("Задача1", Status.NEW, "описаниеЗадачи1");
        Task task2 = new Task("Задача2", Status.NEW, "описаниеЗадачи1");
        Epic epic = new Epic("Эпик1", Status.NEW, "описаниеЗадачи1");
        Epic epic2 = new Epic("Эпик2", Status.NEW, "описаниеЗадачи1");
        taskManager.addTask(task);
        taskManager.addTask(task2);
        taskManager.addEpic(epic);
        taskManager.addEpic(epic2);
        SubTask subTask = new SubTask("Сабтаск1", Status.NEW, "описаниеЗадачи1", epic.getId());
        SubTask subTask1 = new SubTask("Сабтаск2", Status.NEW, "описаниеЗадачи1", epic.getId());
        SubTask subTask2 = new SubTask("Сабтаск3", Status.NEW, "описаниеЗадачи1", epic.getId());
        taskManager.addSub(subTask);
        taskManager.addSub(subTask1);
        taskManager.addSub(subTask2);
        taskManager.getTask(task.getId());
        System.out.println(taskManager.getHistory());
        taskManager.getSub(subTask2.getId());
        System.out.println(taskManager.getHistory());
        taskManager.getEpic(epic.getId());
        System.out.println(taskManager.getHistory());
        taskManager.getTask(task.getId());
        System.out.println(taskManager.getHistory());
        taskManager.deleteTask(task.getId());
        System.out.println(taskManager.getHistory());
        taskManager.deleteEpic(epic.getId());
        System.out.println(taskManager.getHistory());
    }
}