package httpmanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private HttpServer httpServer;
    private TaskManager taskManager;
    private static Gson gson;

    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,new TimeAdapter()).registerTypeAdapter(Duration.class,new DurationAdapter()).create();
    }
    public void stop() {
        httpServer.stop(0);
    }
    public void start() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT),0);
        httpServer.createContext("/tasks", new TaskHandler(taskManager));
        httpServer.createContext("/subtasks", new SubTaskHandler(taskManager));
        httpServer.createContext("/epics", new EpicHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));

        httpServer.start();
        System.out.println("Сервер запущен. Порт: " + PORT);
    }
    public static Gson getGson() {
        return gson;
    }
    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getDefault();
        HttpTaskServer server = new HttpTaskServer(taskManager);
        server.start();
        Task task1 = new Task("Задача1", Status.NEW, "описаниеЗадачи1", Duration.ofMinutes(10), LocalDateTime.now());
        Epic epic = new Epic("Задача2", Status.NEW, "описаниеЗадачи1");
        SubTask subTask = new SubTask("Задача3", Status.NEW, "описаниеЗадачи1", 2, Duration.ofMinutes(5), LocalDateTime.of(2000, 2, 2, 2, 2));
        SubTask subTask2 = new SubTask("Задача4", Status.NEW, "описаниеЗадачи1", 2, Duration.ofMinutes(5), LocalDateTime.of(3000, 2, 2, 2, 2));
        String str = server.gson.toJson(task1);
        String str1 = server.gson.toJson(epic);
        String str2 = server.gson.toJson(subTask);
        String str3 = server.gson.toJson(subTask2);

        System.out.println(str);
        System.out.println(str1);
        System.out.println(str2);
        System.out.println(str3);

    }
}
