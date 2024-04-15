package manager;

import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        Duration duration;
        LocalDateTime startTime;

        String[] taskValues = value.split(",");
        int id = Integer.parseInt(taskValues[0]);
        TaskType type = TaskType.valueOf(taskValues[1]);
        String name = taskValues[2];
        Status status = Status.valueOf(taskValues[3]);
        String description = taskValues[4];
        if(type.equals(TaskType.SUBTASK)) {
            int epicId = Integer.parseInt(taskValues[5]);
            if (taskValues[6]!= null && !taskValues[6].equals("null")) {
                duration = Duration.parse(taskValues[6]);
            } else {
                duration = null;
            }
            if (taskValues[7]!= null && !taskValues[7].equals("null")) {
                startTime = LocalDateTime.parse(taskValues[7], DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } else {
                startTime = null;
            }
            return new SubTask(name, status, description, epicId, id,duration,startTime);
        }
        if (taskValues[5]!= null && !taskValues[5].equals("null")) {
             duration = Duration.parse(taskValues[5]);
        } else {
             duration = null;
        }
        if (taskValues[6]!= null && !taskValues[6].equals("null")) {
            startTime = LocalDateTime.parse(taskValues[6], DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } else {
            startTime = null;
        }
        if (type.equals(TaskType.TASK)) {
            return new Task(name, status, description, id,duration,startTime);
        } else  {
            return new Epic(name, status, description, id,duration,startTime);
        }
    }

    public static String toString(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(task.getId()).append(",");
        sb.append(task.getType()).append(",");
        sb.append(task.getName()).append(",");
        sb.append(task.getStatus()).append(",");
        sb.append(task.getDescription()).append(",");
        if (task.getType().equals(TaskType.SUBTASK)) {
            SubTask subTask = (SubTask) task;
            sb.append(subTask.getEpicId()).append(",");
        }
        sb.append(task.getDuration()).append(",");
        sb.append(task.getStartTime());

        return sb.toString();
    }
}
