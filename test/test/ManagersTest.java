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

import java.util.ArrayList;
import java.util.List;

public class ManagersTest {
    private TaskManager taskManager;
    private HistoryManager historyManager;

    @BeforeEach
    void beforeEach() {
        taskManager = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();
    }
    @Test
    void OneManagerContainsTwoTest() {
        Task task = new Task("Задача1", Status.NEW, "описаниеЗадачи1");
        taskManager.addTask(task);
        historyManager.add(task);
        taskManager.getTask(task.getId());
        ArrayList<Task> list1 = historyManager.getHistory();
        ArrayList<Task> list2 = taskManager.getHistory();
        Assertions.assertEquals(list2,list1);
    }
}
