package test;

import manager.FileBackedTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;

import java.io.File;
import java.io.IOException;

import static manager.FileBackedTaskManager.loadFromFile;

public class FileBackedTaskManagerTest {
    private FileBackedTaskManager fileBackedTaskManager;

    @BeforeEach
    void beforeEach() throws IOException {
        fileBackedTaskManager = new FileBackedTaskManager(File.createTempFile("prefix", "sufix"));
    }

    @Test
    void fileBackedmanagerTest() throws IOException {

        FileBackedTaskManager originalManager = new FileBackedTaskManager(File.createTempFile("prefix", "sufix"));
        originalManager.addTask(new Task("Task1", Status.NEW, "Description1"));
        originalManager.addTask(new Task("Task2", Status.IN_PROGRESS, "Description2"));
        originalManager.addTask(new Task("Task3", Status.DONE, "Description3"));
        originalManager.getTask(1);
        originalManager.getTask(2);
        originalManager.getTask(3);

        FileBackedTaskManager loadedManager = loadFromFile(originalManager.getDataFile());

        Assertions.assertEquals(originalManager.getAllTasks(), loadedManager.getAllTasks());

        Assertions.assertEquals(originalManager.getHistory(), loadedManager.getHistory());
    }
}
