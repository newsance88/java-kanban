package httpmanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;
import tasks.Status;
import tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private HttpServer httpServer;
    private TaskManager taskManager;
    private Gson gson;
    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
    }
    public void start() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT),0);
        httpServer.createContext("/tasks", new TaskHandler());
        httpServer.start();
        System.out.println("Сервер запущен. Порт: " + PORT);
    }

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getDefault();
        HttpTaskServer server = new HttpTaskServer(taskManager);
        server.start();
        Task task1 = new Task("Задача1", Status.NEW, "описаниеЗадачи1", Duration.ofMinutes(10), LocalDateTime.of(2020,8,8,8,8));
        Gson gson1 = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,new TimeAdapter()).create();
        String str = gson1.toJson(task1);
        System.out.println(str);
    }


}
