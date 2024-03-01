package test;

import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.util.List;

public class HistoryManagerTest { //Привет, интересно узнать, много ли времени в реальных проектах уходит на написание тестов?
    private TaskManager taskManager;
    private HistoryManager historyManager;
    @BeforeEach
    void beforeEach() {
        taskManager = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();
    }
    @Test
    void historyManagerTest() {
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
        historyManager.add(task);
        Assertions.assertNotNull(historyManager.getHistory(),"Не пуст");
        Assertions.assertEquals(historyManager.getHistory().size(),1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);
        historyManager.add(task5);
        historyManager.add(task6);
        historyManager.add(task7);
        historyManager.add(task8);
        historyManager.add(task9);
        historyManager.add(task10);
        historyManager.add(task11);
        Assertions.assertEquals(historyManager.getHistory().get(0),task2);

    }
    @Test
    void WhenUpdateTaskInHistoryIsNotTheSame() {
        Task task = new Task("Задача2", Status.NEW, "описаниеЗадачи1");
        taskManager.addTask(task);
        taskManager.getTask(task.getId());
        Task updatedTask = new Task("Обновил2", Status.NEW, "описаниеЗадачи1",task.getId());
        taskManager.updateTask(updatedTask);
        Assertions.assertNotEquals(updatedTask,taskManager.getHistory().get(0));
    }
}
