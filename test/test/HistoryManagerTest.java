package test;

import exceptions.ManagerSaveException;
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
    void historyManagerTest() throws ManagerSaveException {
        Task task = new Task("Задача1", Status.NEW, "описаниеЗадачи1",null,null);
        Task task2 = new Task("Задача2", Status.NEW, "описаниеЗадачи1",null,null);
        Task task3 = new Task("Задача3", Status.NEW, "описаниеЗадачи1",null,null);

        taskManager.addTask(task);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        historyManager.add(task);

        Assertions.assertNotNull(historyManager.getHistory(), "Не пуст");
        Assertions.assertEquals(historyManager.getHistory().size(), 1);
        Assertions.assertEquals(historyManager.getHistory().get(0), task);

        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task.getId());
        Assertions.assertEquals(historyManager.getHistory().get(0), task2);
        Assertions.assertEquals(historyManager.getHistory().get(1), task3);
        historyManager.remove(task2.getId());
        historyManager.remove(task3.getId());

        historyManager.add(task);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task3.getId());
        Assertions.assertEquals(historyManager.getHistory().get(0), task);
        Assertions.assertEquals(historyManager.getHistory().get(1), task2);

        historyManager.remove(task.getId());
        historyManager.remove(task2.getId());

        historyManager.add(task);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task2.getId());
        Assertions.assertEquals(historyManager.getHistory().get(0), task);
        Assertions.assertEquals(historyManager.getHistory().get(1), task3);
    }

    @Test
    void updateTest() throws ManagerSaveException {
        Task task = new Task("Задача2", Status.NEW, "описаниеЗадачи1",null,null);
        taskManager.addTask(task);
        historyManager.add(task);
        Task updatedTask = new Task("Обновил2", Status.NEW, "описаниеЗадачи1", task.getId());
        taskManager.updateTask(updatedTask);
        Assertions.assertNotEquals(updatedTask, historyManager.getHistory().get(0));
    }
}
