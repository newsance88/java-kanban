package test;

import manager.FileBackedTaskManager;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static manager.FileBackedTaskManager.loadFromFile;

public class FileBackedTaskManagerTest {
    private FileBackedTaskManager fileBackedTaskManager;

    @BeforeEach
    void beforeEach() throws IOException {
        fileBackedTaskManager = new FileBackedTaskManager(File.createTempFile("prefix", "sufix"));
    }
    @Test
    void getPrioritizedTasksTest() {
        Task task1 = new Task("Задача1", Status.NEW, "описаниеЗадачи1", Duration.ofMinutes(10), LocalDateTime.now());
        Task task2 = new Task("Задача2", Status.NEW, "описаниеЗадачи1", Duration.ofMinutes(1), LocalDateTime.of(1000,2,2,2,2));
        Epic epic = new Epic("Задача2", Status.NEW, "описаниеЗадачи1");
        SubTask subTask = new SubTask("Задача3", Status.NEW, "описаниеЗадачи1", 2, Duration.ofMinutes(5),LocalDateTime.of(3000,2,2,2,2));
        SubTask subTask2 = new SubTask("Задача4", Status.NEW, "описаниеЗадачи1", 2, Duration.ofMinutes(5),LocalDateTime.of(3000,2,2,2,2));
        fileBackedTaskManager.addTask(task1);
        fileBackedTaskManager.addEpic(epic);
        fileBackedTaskManager.addSub(subTask2);
        fileBackedTaskManager.addTask(task2);
        fileBackedTaskManager.addSub(subTask);
        Assertions.assertEquals(fileBackedTaskManager.getPrioritizedTasks().getFirst(),task2);
        fileBackedTaskManager.removeSubs();
        Assertions.assertEquals(epic.getDuration(),null);
    }
    @Test
    void tasksTimeTest() {
        fileBackedTaskManager.addTask(new Task("name1", Status.NEW, "desr1", Duration.ofMinutes(5), LocalDateTime.of(2004,6,12,12,12)));
        fileBackedTaskManager.addEpic(new Epic("epic1", Status.NEW, "descr2"));
        fileBackedTaskManager.addSub(new SubTask("sub1", Status.NEW, "descr3", 2, Duration.ofMinutes(10), LocalDateTime.now()));
        fileBackedTaskManager.addSub(new SubTask("sub2", Status.NEW, "descr4", 2, Duration.ofMinutes(10), LocalDateTime.now().plus(Duration.ofDays(2))));
        Assertions.assertEquals(fileBackedTaskManager.getTask(1).getStartTime(),LocalDateTime.of(2004,6,12,12,12));
        Assertions.assertEquals(fileBackedTaskManager.getEpic(2).getDuration(),Duration.ofMinutes(20));
        fileBackedTaskManager.removeSubs();
        Assertions.assertNull(fileBackedTaskManager.getEpic(2).getDuration());
    }

    @Test
    void fileBackedmanagerTest() throws IOException {

        FileBackedTaskManager originalManager = new FileBackedTaskManager(File.createTempFile("prefix", "sufix"));
        originalManager.addTask(new Task("Task1", Status.NEW, "Description1",null,null));
        originalManager.addEpic(new Epic("Epic2", Status.IN_PROGRESS, "Description2"));
        originalManager.addSub(new SubTask("SubTask3", Status.DONE, "Description3", 2,null,null));
        originalManager.getTask(1);
        originalManager.getEpic(2);
        originalManager.getSub(3);

        FileBackedTaskManager loadedManager = loadFromFile(originalManager.getDataFile());

        Assertions.assertEquals(originalManager.getAllTasks(), loadedManager.getAllTasks());
        Assertions.assertEquals(originalManager.getAllEpics(), loadedManager.getAllEpics());
        Assertions.assertEquals(originalManager.getAllSubs(), loadedManager.getAllSubs());
        Assertions.assertEquals(originalManager.getHistory(), loadedManager.getHistory());
    }
}
