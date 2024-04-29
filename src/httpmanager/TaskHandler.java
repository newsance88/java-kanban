package httpmanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.Managers;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class TaskHandler extends  BaseHttpHandler implements HttpHandler {
    TaskManager taskManager = Managers.getDefault();
    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,new TimeAdapter()).create();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        switch (method) {
            case "GET" : {
                if (path.split("/").length==3) {
                    handleGet(exchange);
                } else {
                    handleGet(exchange);
                }
                break;
            }
            case "POST" : {
                handlePost(exchange);
                break;
            }
            case "DELETE" : {
                //handleDelete(exchange);
                break;
            }
            default:
                //writeResponse(exchange, "Такого эндпоинта не существует", 404);
        }
    }
    public void handlePost(HttpExchange httpExchange) throws IOException {
        String request = new String(httpExchange.getRequestBody().readAllBytes());
        Task task = gson.fromJson(request, Task.class);
        taskManager.addTask(task);
        sendText(httpExchange,"Задача добавлена");
    }
    public void handleGet(HttpExchange httpExchange) throws IOException {
        List<Task> tasks = taskManager.getAllTasks();
        String str = gson.toJson(tasks);
        sendText(httpExchange,str);
    }
}
