package test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import httpmanager.HttpTaskServer;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManagerTasksTest {
    TaskManager taskManager = Managers.getDefault();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(taskManager);
    Gson gson = HttpTaskServer.getGson();

    @BeforeEach
    public void beforeEach() throws IOException {
        taskManager.removeTasks();
        taskManager.removeSubs();
        taskManager.removeEpics();
        taskServer.start();
    }

    @AfterEach
    public void afterEach() {
        taskServer.stop();
    }

    @Test
    void prioritizedTasksTest() throws IOException, InterruptedException {
        Task task1 = new Task("Задача1", Status.NEW, "описаниеЗадачи1", Duration.ofMinutes(10), LocalDateTime.of(2020, 8, 8, 8, 8));
        Task task2 = new Task("Задача1", Status.NEW, "описаниеЗадачи1", Duration.ofMinutes(10), LocalDateTime.of(2014, 2, 2, 2, 2));
        Task task3 = new Task("Задача1", Status.NEW, "описаниеЗадачи1", Duration.ofMinutes(10), LocalDateTime.of(2030, 2, 2, 2, 2));

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized/");
        HttpRequest request = HttpRequest.newBuilder(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> list = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>() {
        }.getType());
        Assertions.assertEquals(list, taskManager.getPrioritizedTasks());
    }

    @Test
    public void historyTest() throws IOException, InterruptedException {
        Task task = new Task("Задача1", Status.NEW, "описаниеЗадачи1", Duration.ofMinutes(10), LocalDateTime.of(2021, 8, 8, 8, 8));
        Task task2 = new Task("Задача2", Status.NEW, "описаниеЗадачи1", Duration.ofMinutes(10), LocalDateTime.of(2022, 8, 8, 8, 8));
        Task task3 = new Task("Задача3", Status.NEW, "описаниеЗадачи1", Duration.ofMinutes(10), LocalDateTime.of(2023, 8, 8, 8, 8));
        taskManager.addTask(task);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.getTask(2);
        taskManager.getTask(1);
        taskManager.getTask(3);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history/");
        HttpRequest request = HttpRequest.newBuilder(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> list = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>() {
        }.getType());
        System.out.println(list);
        Assertions.assertEquals(list, taskManager.getHistory());
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    public void testAddSub() throws IOException, InterruptedException {
        Epic epic = new Epic("Задача2", Status.NEW, "описаниеЗадачи1");
        SubTask subTask = new SubTask("Задача3", Status.NEW, "описаниеЗадачи1", 1, Duration.ofMinutes(5), LocalDateTime.of(2000, 2, 2, 2, 2));
        String epicJson = gson.toJson(epic);
        String subTaskJson = gson.toJson(subTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/epics/");
        URI url2 = URI.create("http://localhost:8080/subtasks/");

        HttpRequest request = HttpRequest.newBuilder().uri(url1).header("Content-Type", "application/json;charset=utf-8").POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).header("Content-Type", "application/json;charset=utf-8").POST(HttpRequest.BodyPublishers.ofString(subTaskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(200, response2.statusCode());

        List<Epic> tasksFromManager = taskManager.getAllEpics();
        List<SubTask> tasksFromManager2 = taskManager.getAllSubs();

        Assertions.assertNotNull(tasksFromManager, "Задачи не возвращаются");
        Assertions.assertNotNull(tasksFromManager, "Задачи не возвращаются");

        Assertions.assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        Assertions.assertEquals(1, tasksFromManager2.size(), "Некорректное количество задач");

        Assertions.assertEquals("Задача2", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
        Assertions.assertEquals("Задача3", tasksFromManager2.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Задача2", Status.NEW, "описаниеЗадачи1");
        String epicJson = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).header("Content-Type", "application/json;charset=utf-8").POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());

        List<Epic> tasksFromManager = taskManager.getAllEpics();

        Assertions.assertNotNull(tasksFromManager, "Задачи не возвращаются");

        Assertions.assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");

        Assertions.assertEquals("Задача2", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Задача1", Status.NEW, "описаниеЗадачи1", Duration.ofMinutes(10), LocalDateTime.of(2020, 8, 8, 8, 8));
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).header("Content-Type", "application/json;charset=utf-8").POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        List<Task> tasksFromManager = taskManager.getAllTasks();
        Assertions.assertNotNull(tasksFromManager, "Задачи не возвращаются");
        Assertions.assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        Assertions.assertEquals("Задача1", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }
}
