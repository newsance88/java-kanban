package manager;

import tasks.*;

import java.util.ArrayList;
import java.util.List;

public class FormatManager {
    public static String historyToString(ArrayList<Task> historyList) {
        StringBuilder history = new StringBuilder();
        for (int i = 0; i < historyList.size(); i++) {
            if (i == 0) {
                history.append(historyList.get(i).getId());
            } else {
                history.append(",").append(historyList.get(i).getId());
            }
        }
        return history.toString();
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        if (value != null) {
            String[] values = value.split(",");
            if (values.length >= 2) {
                for (String id : values) {
                    history.add(Integer.valueOf(id));
                }
            }
        }
        return history;
    }

    public static Task fromString(String value) {
        if (!value.isEmpty() && !value.startsWith("id")) {
            String[] taskValues = value.split(",");
            if (taskValues.length >= 2 && !value.startsWith("id") && (value.contains("EPIC") || value.contains("TASK") || value.contains("SUBTASK"))) {
                int id = Integer.parseInt(taskValues[0]);
                TaskType type = TaskType.valueOf(taskValues[1]);
                String name = taskValues[2];
                Status status = Status.valueOf(taskValues[3]);
                String description = taskValues[4];
                if (type.equals(TaskType.TASK)) {
                    return new Task(name, status, description, id);
                } else if (type.equals(TaskType.EPIC)) {
                    return new Epic(name, status, description, id);
                } else if (type.equals(TaskType.SUBTASK)) {
                    int epicId = Integer.parseInt(taskValues[5]);
                    return new SubTask(name, status, description, epicId, id);
                }
            }

        }
        return null;
    }

    public static String toString(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(task.getId()).append(",");
        sb.append(task.getType()).append(",");
        sb.append(task.getName()).append(",");
        sb.append(task.getStatus()).append(",");
        sb.append(task.getDescription());

        if (task.getType().equals(TaskType.SUBTASK)) {
            SubTask subTask = (SubTask) task;
            sb.append(",").append(subTask.getEpicId());
        }

        return sb.toString();
    }
}
