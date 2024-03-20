package test;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;

public class HistoryManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    void historyManagerTest() { //Я поменял здесь все на taskManager, потому что в taskManager и реализуется historyManager, и по сути мы в основном будем пользоваться как раз taskManager
        Task task = new Task("Задача1", Status.NEW, "описаниеЗадачи1");
        Task task2 = new Task("Задача2", Status.NEW, "описаниеЗадачи1");
        Task task3 = new Task("Задача3", Status.NEW, "описаниеЗадачи1");
        Task task4 = new Task("Задача4", Status.NEW, "описаниеЗадачи1");
        Task task5 = new Task("Задача5", Status.NEW, "описаниеЗадачи1");
        Task task6 = new Task("Задача6", Status.NEW, "описаниеЗадачи1");
        Task task7 = new Task("Задача7", Status.NEW, "описаниеЗадачи1");
        Task task8 = new Task("Задача8", Status.NEW, "описаниеЗадачи1");
        Task task9 = new Task("Задача9", Status.NEW, "описаниеЗадачи1");
        Task task10 = new Task("Задача10", Status.NEW, "описаниеЗадачи1");
        Task task11 = new Task("Задача11", Status.NEW, "описаниеЗадачи1");
        Task task12 = new Task("Задача12", Status.NEW, "описаниеЗадачи1");
        taskManager.addTask(task);
        taskManager.getTask(task.getId());
        Assertions.assertNotNull(taskManager.getHistory(), "Не пуст");
        Assertions.assertEquals(taskManager.getHistory().size(), 1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.addTask(task4);
        taskManager.addTask(task5);
        taskManager.addTask(task6);
        taskManager.addTask(task7);
        taskManager.addTask(task8);
        taskManager.addTask(task9);
        taskManager.addTask(task10);
        taskManager.addTask(task11);
        taskManager.addTask(task12);
        taskManager.getTask(task2.getId());
        taskManager.getTask(task3.getId());
        taskManager.getTask(task4.getId());
        taskManager.getTask(task5.getId());
        taskManager.getTask(task6.getId());
        taskManager.getTask(task7.getId());
        taskManager.getTask(task8.getId());
        taskManager.getTask(task9.getId());
        taskManager.getTask(task10.getId());
        taskManager.getTask(task11.getId());
        taskManager.getTask(task12.getId());

        Assertions.assertEquals(taskManager.getHistory().get(0), task);
        Assertions.assertEquals(taskManager.getHistory().size(), 12);

        taskManager.getHistoryManager().remove(task12.getId());
        Assertions.assertEquals(taskManager.getHistory().size(), 11);

        taskManager.getTask(task.getId());
        Assertions.assertEquals(taskManager.getHistory().getLast(), task);
    }

    @Test
    void updateTest() {
        Task task = new Task("Задача2", Status.NEW, "описаниеЗадачи1");
        taskManager.addTask(task);
        taskManager.getTask(task.getId());
        Task updatedTask = new Task("Обновил2", Status.NEW, "описаниеЗадачи1", task.getId());
        taskManager.updateTask(updatedTask);
        Assertions.assertNotEquals(updatedTask, taskManager.getHistory().get(0));
    }
}
