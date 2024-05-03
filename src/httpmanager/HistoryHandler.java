package httpmanager;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.ArrayList;

public class HistoryHandler extends BaseHttpHandler {
    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

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
                sendErrorText(exchange, "Неверный запрос", 405);
        }
    }

    private void handleGet(HttpExchange httpExchange) throws IOException {
        ArrayList<Task> tasks = taskManager.getHistory();
        String str = gson.toJson(tasks);
        sendText(httpExchange, str);
    }
}
