package httpmanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new TimeAdapter()).registerTypeAdapter(Duration.class, new DurationAdapter()).create();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        switch (method) {
            case "GET": {
                handleGet(exchange);
                break;
            }
            default:
                sendErrorText(exchange, "Неверный запрос", 404);
        }
    }

    private void handleGet(HttpExchange httpExchange) throws IOException {
        List<Task> tasks = taskManager.getPrioritizedTasks();
        String str = gson.toJson(tasks);
        sendText(httpExchange, str);
    }
}
