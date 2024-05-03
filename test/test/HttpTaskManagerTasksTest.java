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
    Gson gson = Managers.createGsonWithAdapters();

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
    public void getAllSubs() throws IOException, InterruptedException {
        Epic task = new Epic("Задача1", Status.NEW, "описаниеЗадачи1");
        SubTask subTask = new SubTask("Задача2", Status.NEW, "описаниеЗадачи1", 1, null, null);
        SubTask subTask2 = new SubTask("Задача3", Status.NEW, "описаниеЗадачи1", 1, null, null);

        taskManager.addEpic(task);
        taskManager.addSub(subTask);
        taskManager.addSub(subTask2);
        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/subtasks/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<SubTask> retrievedList = gson.fromJson(response.body(), new TypeToken<ArrayList<SubTask>>() {
        }.getType());

        Assertions.assertEquals(200, response.statusCode(), "Статус код ответа некорректен");
        Assertions.assertEquals(retrievedList, taskManager.getAllSubs());
    }

    @Test
    public void getAllEpic() throws IOException, InterruptedException {
        Epic task = new Epic("Задача1", Status.NEW, "описаниеЗадачи1");
        Epic task2 = new Epic("Задача2", Status.NEW, "описаниеЗадачи1");
        taskManager.addEpic(task);
        taskManager.addEpic(task2);
        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/epics/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Epic> retrievedList = gson.fromJson(response.body(), new TypeToken<ArrayList<Epic>>() {
        }.getType());

        Assertions.assertEquals(200, response.statusCode(), "Статус код ответа некорректен");
        Assertions.assertEquals(retrievedList, taskManager.getAllEpics());
    }

    @Test
    public void getAllTask() throws IOException, InterruptedException {
        Task task = new Task("Задача1", Status.NEW, "описаниеЗадачи1", null, null);
        Task task2 = new Task("Задача2", Status.NEW, "описаниеЗадачи1", null, null);
        taskManager.addTask(task);
        taskManager.addTask(task2);
        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> retrievedList = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>() {
        }.getType());

        Assertions.assertEquals(200, response.statusCode(), "Статус код ответа некорректен");
        Assertions.assertEquals(retrievedList, taskManager.getAllTasks());
    }

    @Test
    public void getSubById() throws IOException, InterruptedException {
        Epic task = new Epic("Задача1", Status.NEW, "описаниеЗадачи1");
        taskManager.addEpic(task);

        int taskId = task.getId();
        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode(), "Статус код ответа некорректен");
        Epic retrievedTask = gson.fromJson(response.body(), Epic.class);
        Assertions.assertNotNull(retrievedTask, "Задача не была получена");

        Assertions.assertEquals(taskId, retrievedTask.getId(), "ID полученной задачи не совпадает с ожидаемым");

        Assertions.assertEquals(task, retrievedTask);
    }

    @Test
    public void getEpicById() throws IOException, InterruptedException {
        Epic task = new Epic("Задача1", Status.NEW, "описаниеЗадачи1");
        SubTask subTask = new SubTask("Задача3", Status.NEW, "описаниеЗадачи1", 1, null, null);
        taskManager.addEpic(task);
        taskManager.addSub(subTask);

        int taskId = subTask.getId();
        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode(), "Статус код ответа некорректен");
        SubTask retrievedTask = gson.fromJson(response.body(), SubTask.class);
        Assertions.assertNotNull(retrievedTask, "Задача не была получена");

        Assertions.assertEquals(taskId, retrievedTask.getId(), "ID полученной задачи не совпадает с ожидаемым");

        Assertions.assertEquals(subTask, retrievedTask);
    }

    @Test
    public void getTaskById() throws IOException, InterruptedException {
        Task task = new Task("Задача1", Status.NEW, "описаниеЗадачи1", null, null);
        taskManager.addTask(task);

        int taskId = task.getId();
        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/tasks/1");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode(), "Статус код ответа некорректен");

        Task retrievedTask = gson.fromJson(response.body(), Task.class);

        Assertions.assertNotNull(retrievedTask, "Задача не была получена");

        Assertions.assertEquals(taskId, retrievedTask.getId(), "ID полученной задачи не совпадает с ожидаемым");

        Assertions.assertEquals(task, retrievedTask);
    }

    @Test
    public void updateSub() throws IOException, InterruptedException {
        Epic task = new Epic("Задача1", Status.NEW, "описаниеЗадачи1");
        SubTask subTask = new SubTask("Задача3", Status.NEW, "описаниеЗадачи1", 1, null, null);

        taskManager.addEpic(task);
        taskManager.addSub(subTask);

        SubTask updatedTask = new SubTask("ЗадачаОбновленная", Status.NEW, "описаниеЗадачи1", 1, 2);
        String updatedTaskJson = gson.toJson(updatedTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/2");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(updatedTaskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode(), "Статус код ответа некорректен");

        Assertions.assertEquals(taskManager.getSub(2), updatedTask);
        Assertions.assertNotEquals(task, taskManager.getSub(2));
    }

    @Test
    public void updateEpic() throws IOException, InterruptedException {
        Epic task = new Epic("Задача1", Status.NEW, "описаниеЗадачи1");

        taskManager.addEpic(task);

        Epic updatedTask = new Epic("ЗадачаОбновленная", Status.NEW, "описаниеЗадачи1", task.getId());
        String updatedTaskJson = gson.toJson(updatedTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(updatedTaskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode(), "Статус код ответа некорректен");

        Assertions.assertEquals(taskManager.getEpic(updatedTask.getId()), updatedTask);
        Assertions.assertNotEquals(task, taskManager.getEpic(1));
    }

    @Test
    public void updateTask() throws IOException, InterruptedException {
        Task task = new Task("Задача1", Status.NEW, "описаниеЗадачи1", null, null);

        taskManager.addTask(task);

        Task updatedTask = new Task("ЗадачаОбновленная", Status.NEW, "описаниеЗадачи1", task.getId());
        String updatedTaskJson = gson.toJson(updatedTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(updatedTaskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode(), "Статус код ответа некорректен");

        Assertions.assertEquals(taskManager.getTask(updatedTask.getId()), updatedTask);
        Assertions.assertNotEquals(task, taskManager.getTask(1));
    }

    @Test
    public void testDeleteSub() throws IOException, InterruptedException {
        Epic epic = new Epic("Задача2", Status.NEW, "описаниеЗадачи1");
        SubTask subTask = new SubTask("Задача3", Status.NEW, "описаниеЗадачи1", 1, Duration.ofMinutes(5), LocalDateTime.of(2000, 2, 2, 2, 2));
        taskManager.addEpic(epic);
        taskManager.addSub(subTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode(), "Статус код ответа некорректен");

        List<SubTask> tasksFromManager = taskManager.getAllSubs();
        Assertions.assertTrue(tasksFromManager.isEmpty(), "Сабтаск не был удален");
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        Task task = new Task("Задача1", Status.NEW, "описаниеЗадачи1", Duration.ofMinutes(10), LocalDateTime.of(2020, 8, 8, 8, 8));
        taskManager.addTask(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode(), "Статус код ответа некорректен");

        List<Task> tasksFromManager = taskManager.getAllTasks();
        Assertions.assertTrue(tasksFromManager.isEmpty(), "Таск не был удален");
    }

    @Test
    public void testDeleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Задача2", Status.NEW, "описаниеЗадачи1");
        taskManager.addEpic(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode(), "Статус код ответа некорректен");

        List<Epic> tasksFromManager = taskManager.getAllEpics();
        Assertions.assertTrue(tasksFromManager.isEmpty(), "Эпик не был удален");
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
