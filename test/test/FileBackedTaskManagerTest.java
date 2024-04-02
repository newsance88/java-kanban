package test;

import exceptions.ManagerSaveException;
import manager.FileBackedTaskManager;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static manager.FileBackedTaskManager.loadFromFile;

public class FileBackedTaskManagerTest {
    private FileBackedTaskManager fileBackedTaskManager;

    @BeforeEach
    void beforeEach() throws IOException {
        fileBackedTaskManager = new FileBackedTaskManager(File.createTempFile("prefix", "sufix"));
    }

    @Test
    void historyTest() throws ManagerSaveException {
        fileBackedTaskManager.addTask(new Task("name1", Status.NEW, "desr1"));
        fileBackedTaskManager.addTask(new Epic("epic1", Status.NEW, "descr2"));
        fileBackedTaskManager.addTask(new SubTask("sub1", Status.NEW, "descr3", 2));
        fileBackedTaskManager.getTask(1);
        fileBackedTaskManager.getTask(3);

        ArrayList<String> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileBackedTaskManager.getDataFile()))) {

            while (reader.ready()) {
                String line = reader.readLine();
                list.add(line);
            }
        } catch (IOException e) {

        }
        Assertions.assertEquals(list.get(list.size() - 1), "1,3");

        fileBackedTaskManager.deleteTask(1);

        try (BufferedReader reader = new BufferedReader(new FileReader(fileBackedTaskManager.getDataFile()))) {

            while (reader.ready()) {
                String line = reader.readLine();
                list.add(line);
            }
        } catch (IOException e) {

        }
        Assertions.assertEquals(list.get(list.size() - 1), "3");
    }

    @Test
    void fromHistoryToHistoryTest() throws ManagerSaveException {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task("Задача1", Status.NEW, "описаниеЗадачи1");
        Task task2 = new Task("Задача2", Status.NEW, "описаниеЗадачи1");
        Task task3 = new Task("Задача3", Status.NEW, "описаниеЗадачи1");
        taskManager.addTask(task);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        historyManager.add(task);
        historyManager.add(task2);
        historyManager.add(task3);
        Assertions.assertEquals(fileBackedTaskManager.historyToString(historyManager), "1,2,3");
        String str = fileBackedTaskManager.historyToString(historyManager);
        Assertions.assertEquals(fileBackedTaskManager.historyFromString(str), new ArrayList<Integer>(Arrays.asList(1, 2, 3)));
    }

    @Test
    void loadFromFileTest() throws ManagerSaveException {
        FileBackedTaskManager fileBackedTaskManager = loadFromFile(new File("C:\\Users\\Владелец\\Documents\\GitHub\\java-kanban\\test\\test\\DataTest"));
        Task task = new Task("name1", Status.NEW, "desr1", 1);
        Assertions.assertEquals(task, fileBackedTaskManager.getTask(1));
    }

    @Test
    void fromStringToStringTest() throws ManagerSaveException {
        Task task = new Task("name1", Status.NEW, "desr1");
        fileBackedTaskManager.addTask(task);
        Assertions.assertEquals(task, fileBackedTaskManager.fromString("1,Task,name1,NEW,desr1"));
        Assertions.assertEquals(fileBackedTaskManager.toString(task), "1,Task,name1,NEW,desr1");
    }
}
