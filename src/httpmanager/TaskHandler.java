package httpmanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.TasksCrossException;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new TimeAdapter()).registerTypeAdapter(Duration.class, new DurationAdapter()).create();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        switch (method) {
            case "GET": {
                if (path.split("/").length == 3) {
                    handleGetId(exchange, Integer.parseInt(path.split("/")[2]));
                } else {
                    handleGet(exchange);
                }
                break;
            }
            case "POST": {
                handlePost(exchange);
                break;
            }
            case "DELETE": {
                handleDelete(exchange);
                break;
            }
            default:
                sendErrorText(exchange, "Неверный запрос", 404);
        }
    }

    private void handleDelete(HttpExchange httpExchange) throws IOException {
        taskManager.removeTasks();
        sendText(httpExchange, "Задачи удалены");
    }

    private void handlePost(HttpExchange httpExchange) throws IOException {
        String request = new String(httpExchange.getRequestBody().readAllBytes());
        Type mapType = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> map = gson.fromJson(request, mapType);
        if (map.get("id") == null || map.get("id").equals("0")) {
            Task task = gson.fromJson(request, Task.class);
            try {
                taskManager.addTask(task);
                sendText(httpExchange, "Задача добавлена");

            } catch (TasksCrossException e) {
                sendErrorText(httpExchange, "Задачи пересекаются", 406);
            }
        } else {
            int taskId = Integer.parseInt(map.get("id"));
            Task task = gson.fromJson(request, Task.class);
            task.setId(taskId);
            taskManager.updateTask(task);
            sendText(httpExchange, "Задача обновлена");
        }
    }

    private void handleGet(HttpExchange httpExchange) throws IOException {
        List<Task> tasks = taskManager.getAllTasks();
        String str = gson.toJson(tasks);
        sendText(httpExchange, str);
    }

    private void handleGetId(HttpExchange httpExchange, int id) throws IOException {
        Task task = taskManager.getTask(id);
        if (task == null) {
            sendErrorText(httpExchange, "Задача не существует", 404);
        } else {
            String str = gson.toJson(task);
            sendText(httpExchange, str);
        }
    }
}
