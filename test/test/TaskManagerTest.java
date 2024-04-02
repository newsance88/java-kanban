package test;

import exceptions.ManagerSaveException;
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

public class TaskManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    void addNewTasks() throws ManagerSaveException {
        Task task = new Task("Задача1", Status.NEW, "описаниеЗадачи1");
        taskManager.addTask(task);
        final Task savedTask = taskManager.getTask(task.getId());
        Assertions.assertNotNull(savedTask, "Задача не найдена.");
        Assertions.assertEquals(task, savedTask, "Задачи не совпадают.");
        final ArrayList<Task> tasks = taskManager.getAllTasks();
        Assertions.assertNotNull(tasks, "Задачи не возвращаются.");
        Assertions.assertEquals(1, tasks.size(), "Неверное количество задач.");
        Assertions.assertEquals(task, tasks.get(0), "Задачи не совпадают.");

        Epic epic = new Epic("Задача1", Status.NEW, "описаниеЗадачи1");
        taskManager.addEpic(epic);
        final Epic savedEpic = taskManager.getEpic(epic.getId());
        Assertions.assertNotNull(savedEpic, "Задача не найдена.");
        Assertions.assertEquals(epic, savedEpic, "Задачи не совпадают.");
        final ArrayList<Epic> epics = taskManager.getAllEpics();
        Assertions.assertNotNull(epics, "Задачи не возвращаются.");
        Assertions.assertEquals(1, epics.size(), "Неверное количество задач.");
        Assertions.assertEquals(epic, epics.get(0), "Задачи не совпадают.");

        SubTask subTask = new SubTask("Задача1", Status.NEW, "описаниеЗадачи1", epic.getId());
        taskManager.addSub(subTask);
        final SubTask savedSub = taskManager.getSub(subTask.getId());
        Assertions.assertNotNull(savedSub, "Задача не найдена.");
        Assertions.assertEquals(subTask, savedSub, "Задачи не совпадают.");
        final ArrayList<SubTask> subTasks = taskManager.getAllSubs();
        Assertions.assertNotNull(subTasks, "Задачи не возвращаются.");
        Assertions.assertEquals(1, subTasks.size(), "Неверное количество задач.");
        Assertions.assertEquals(subTask, subTasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void testUpdateTasks() throws ManagerSaveException {
        Task task = new Task("Задача1", Status.NEW, "описаниеЗадачи1");

        taskManager.addTask(task);
        taskManager.getTask(task.getId());
        Assertions.assertNotNull(taskManager.getHistory(), "Не пуст");
        Assertions.assertEquals(taskManager.getHistory().size(), 1);
        Epic epic = new Epic("Эпик1", Status.NEW, "описаниеЗадачи1");

        taskManager.addEpic(epic);
        taskManager.getEpic(epic.getId());
        SubTask subTask = new SubTask("Сабтаск1", Status.NEW, "описаниеЗадачи1", epic.getId());
        taskManager.addSub(subTask);
        taskManager.getSub(subTask.getId());
        Assertions.assertEquals(taskManager.getHistory().size(), 3);

        Task updatedTask = new Task("ЗадачаОбновленная", Status.NEW, "описаниеЗадачи1", task.getId());
        taskManager.updateTask(updatedTask);
        Assertions.assertEquals(taskManager.getTask(updatedTask.getId()), updatedTask);
        Assertions.assertNotEquals(task, updatedTask);

        Epic updatedEpic = new Epic("ЭпикОбновленная", Status.NEW, "описаниеЗадачи1", epic.getId());
        taskManager.updateEpic(updatedEpic);
        Assertions.assertEquals(taskManager.getEpic(updatedEpic.getId()), updatedEpic);
        Assertions.assertNotEquals(epic, updatedEpic);

        SubTask updatedSub = new SubTask("СабТаскОбновленная", Status.NEW, "описаниеЗадачи1", updatedEpic.getId(), subTask.getId());
        taskManager.updateSub(updatedSub);
        Assertions.assertEquals(taskManager.getSub(updatedSub.getId()), updatedSub);
        Assertions.assertNotEquals(subTask, updatedSub);

        taskManager.removeTasks();
        taskManager.removeEpics();
        taskManager.removeSubs();
        Assertions.assertEquals(taskManager.getAllTasks(), new ArrayList<Task>());
        Assertions.assertEquals(taskManager.getAllEpics(), new ArrayList<Task>());
        Assertions.assertEquals(taskManager.getAllSubs(), new ArrayList<Task>());
    }

    @Test
    void equalsTest() {
        Task task1 = new Task("Задача1", Status.NEW, "описаниеЗадачи1");
        Task task2 = new Task("Задача2", Status.NEW, "описаниеЗадачи2");
        Task task3 = new Task("Задача1", Status.NEW, "описаниеЗадачи1");
        Assertions.assertEquals(task1, task3, "Не равны");
        Assertions.assertNotEquals(task1, task2);
        task1.setName("Задача11");
        task3.setName("Задача11");
        Assertions.assertEquals(task1, task3, "Не равны");
        task3.setName("Задача13");
        Assertions.assertNotEquals(task1, task3, "Не равны");
    }

}
