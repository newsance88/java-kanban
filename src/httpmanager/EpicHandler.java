package httpmanager;

import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import exceptions.TasksCrossException;
import manager.TaskManager;
import tasks.Epic;
import tasks.SubTask;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class EpicHandler extends BaseHttpHandler {
    public EpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        switch (method) {
            case "GET": {
                if (path.split("/").length == 3) {
                    handleGetId(exchange, Integer.parseInt(path.split("/")[2]));
                } else if (path.split("/").length == 4) {
                    handleGetSubs(exchange, Integer.parseInt(path.split("/")[2]));
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
                sendErrorText(exchange, "Неверный запрос", 405);
        }
    }

    private void handleDelete(HttpExchange httpExchange) throws IOException {
        taskManager.removeEpics();
        sendText(httpExchange, "Задачи удалены");
    }

    private void handlePost(HttpExchange httpExchange) throws IOException {
        String request = new String(httpExchange.getRequestBody().readAllBytes());
        Type mapType = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> map = gson.fromJson(request, mapType);
        if (map.get("id") == null || map.get("id").equals("0")) {
            Epic task = gson.fromJson(request, Epic.class);
            try {
                taskManager.addEpic(task);
                sendText(httpExchange, "Задача добавлена");
            } catch (TasksCrossException e) {
                sendErrorText(httpExchange, "Задачи пересекаются", 406);
            }
        } else {
            int taskId = Integer.parseInt(map.get("id"));
            Epic task = gson.fromJson(request, Epic.class);
            task.setId(taskId);
            taskManager.updateEpic(task);
            sendText(httpExchange, "Задача обновлена");
        }
    }

    private void handleGet(HttpExchange httpExchange) throws IOException {
        List<Epic> tasks = taskManager.getAllEpics();
        String str = gson.toJson(tasks);
        sendText(httpExchange, str);
    }

    private void handleGetId(HttpExchange httpExchange, int id) throws IOException {
        Epic task = taskManager.getEpic(id);
        if (task == null) {
            sendErrorText(httpExchange, "Задача не существует", 404);
        } else {
            String str = gson.toJson(task);
            sendText(httpExchange, str);
        }
    }

    private void handleGetSubs(HttpExchange httpExchange, int id) throws IOException {
        Epic task = taskManager.getEpic(id);
        if (task == null) {
            sendErrorText(httpExchange, "Задача не существует", 404);
        } else {
            List<SubTask> tasks = taskManager.getSubsFromEpic(id);
            String str = gson.toJson(tasks);
            sendText(httpExchange, str);
        }
    }
}

