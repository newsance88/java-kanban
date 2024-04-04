package manager;

import tasks.*;

import java.util.ArrayList;
import java.util.List;

public class FormatManager {
    public static String historyToString(HistoryManager manager) {
        StringBuilder history = new StringBuilder();
        for (int i = 0; i < manager.getHistory().size(); i++) {
            if (i == 0) {
                history.append(manager.getHistory().get(i).getId());
            } else {
                history.append(",").append(manager.getHistory().get(i).getId());
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
            if (taskValues.length >= 2) {
                if (taskValues[1].equals("TASK")) {
                    Task task = new Task("", Status.NEW, "");
                    task.setId(Integer.parseInt(taskValues[0]));
                    task.setName(taskValues[2]);
                    task.setStatus(Status.valueOf(taskValues[3]));
                    task.setDescription(taskValues[4]);
                    return task;
                } else if (taskValues[1].equals("EPIC")) {
                    Epic epic = new Epic("", Status.NEW, "");
                    epic.setId(Integer.parseInt(taskValues[0]));
                    epic.setName(taskValues[2]);
                    epic.setStatus(Status.valueOf(taskValues[3]));
                    epic.setDescription(taskValues[4]);
                    return epic;
                } else if (taskValues[1].equals("SUBTASK")) {
                    SubTask subTask = new SubTask("", Status.NEW, "", 0);
                    subTask.setId(Integer.parseInt(taskValues[0]));
                    subTask.setName(taskValues[2]);
                    subTask.setStatus(Status.valueOf(taskValues[3]));
                    subTask.setDescription(taskValues[4]);
                    subTask.setEpicId(Integer.parseInt(taskValues[5]));
                    return subTask;
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
