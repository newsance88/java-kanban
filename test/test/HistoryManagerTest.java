package test;

import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;

public class HistoryManagerTest {
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
        taskManager.addTask(task);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.addTask(task4);
        taskManager.addTask(task5);
        historyManager.add(task);

        Assertions.assertNotNull(historyManager.getHistory(), "Не пуст");
        Assertions.assertEquals(historyManager.getHistory().size(), 1);
        Assertions.assertEquals(historyManager.getHistory().get(0), task);

        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);
        historyManager.add(task5);
        Assertions.assertEquals(historyManager.getHistory().size(), 5);

        historyManager.remove(task.getId());

        Assertions.assertEquals(historyManager.getHistory().size(), 4);
        Assertions.assertFalse(historyManager.getHistory().contains(task));

        historyManager.remove(task5.getId());
        Assertions.assertFalse(historyManager.getHistory().contains(task5));

        historyManager.remove(task3.getId());
        Assertions.assertFalse(historyManager.getHistory().contains(task3));
    }

    @Test
    void updateTest() {
        Task task = new Task("Задача2", Status.NEW, "описаниеЗадачи1");
        taskManager.addTask(task);
        historyManager.add(task);
        Task updatedTask = new Task("Обновил2", Status.NEW, "описаниеЗадачи1", task.getId());
        taskManager.updateTask(updatedTask);
        Assertions.assertNotEquals(updatedTask, historyManager.getHistory().get(0));
    }
}
