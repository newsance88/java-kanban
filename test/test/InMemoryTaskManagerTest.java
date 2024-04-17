package test;

import exceptions.ManagerSaveException;
import manager.InMemoryTaskManager;
import manager.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class InMemoryTaskManagerTest {
    private InMemoryTaskManager taskManager;

    @BeforeEach
    void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    void statusTest() {
        Task task1 = new Task("Задача1", Status.NEW, "описаниеЗадачи1", Duration.ofMinutes(10), LocalDateTime.now());
        Epic epic = new Epic("Задача2", Status.NEW, "описаниеЗадачи1");
        SubTask subTask = new SubTask("Задача3", Status.NEW, "описаниеЗадачи1", 2, Duration.ofMinutes(5), LocalDateTime.of(2000, 2, 2, 2, 2));
        SubTask subTask2 = new SubTask("Задача4", Status.NEW, "описаниеЗадачи1", 2, Duration.ofMinutes(5), LocalDateTime.of(3000, 2, 2, 2, 2));
        taskManager.addTask(task1);
        taskManager.addEpic(epic);
        taskManager.addSub(subTask);
        taskManager.addSub(subTask2);
        Assertions.assertEquals(taskManager.getEpic(2).getStatus(), Status.NEW);
        SubTask subTask3 = new SubTask("Задача3", Status.IN_PROGRESS, "описаниеЗадачи1", 2, Duration.ofMinutes(5), LocalDateTime.of(4000, 2, 2, 2, 2));
        taskManager.addSub(subTask3);
        Assertions.assertEquals(taskManager.getEpic(2).getStatus(), Status.IN_PROGRESS);
        taskManager.removeSubs();
        SubTask subTask4 = new SubTask("Задача4", Status.DONE, "описаниеЗадачи1", 2, Duration.ofMinutes(5), LocalDateTime.of(4000, 2, 2, 2, 2));
        taskManager.addSub(subTask4);
        Assertions.assertEquals(taskManager.getEpic(2).getStatus(), Status.DONE);
    }

    @Test
    void CrossTest() {
        Task task1 = new Task("Задача1", Status.NEW, "описаниеЗадачи1", Duration.ofMinutes(10), LocalDateTime.of(1000, 10, 10, 10, 10));
        Task task2 = new Task("Задача2", Status.NEW, "описаниеЗадачи1", Duration.ofMinutes(10), LocalDateTime.of(2000, 10, 10, 10, 10));
        Assertions.assertTrue(taskManager.ifTasksNotCross(task1, task2));

        taskManager.addTask(task1);
        taskManager.addTask(task2);
    }

    @Test
    void addNewTasks() throws ManagerSaveException {
        Task task = new Task("Задача1", Status.NEW, "описаниеЗадачи1", null, null);
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

        SubTask subTask = new SubTask("Задача1", Status.NEW, "описаниеЗадачи1", epic.getId(), null, null);
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
        Task task = new Task("Задача1", Status.NEW, "описаниеЗадачи1", null, null);

        taskManager.addTask(task);
        taskManager.getTask(task.getId());
        Assertions.assertNotNull(taskManager.getHistory(), "Не пуст");
        Assertions.assertEquals(taskManager.getHistory().size(), 1);
        Epic epic = new Epic("Эпик1", Status.NEW, "описаниеЗадачи1");

        taskManager.addEpic(epic);
        taskManager.getEpic(epic.getId());
        SubTask subTask = new SubTask("Сабтаск1", Status.NEW, "описаниеЗадачи1", epic.getId(), null, null);
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
        Task task1 = new Task("Задача1", Status.NEW, "описаниеЗадачи1", null, null);
        Task task2 = new Task("Задача2", Status.NEW, "описаниеЗадачи2", null, null);
        Task task3 = new Task("Задача1", Status.NEW, "описаниеЗадачи1", null, null);
        Assertions.assertEquals(task1, task3, "Не равны");
        Assertions.assertNotEquals(task1, task2);
        task1.setName("Задача11");
        task3.setName("Задача11");
        Assertions.assertEquals(task1, task3, "Не равны");
        task3.setName("Задача13");
        Assertions.assertNotEquals(task1, task3, "Не равны");
    }

}
