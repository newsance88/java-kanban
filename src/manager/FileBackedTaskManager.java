package manager;


import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    public static void main(String[] args) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();
        fileBackedTaskManager.addTask(new Task("name1", Status.NEW,"desr1"));
        fileBackedTaskManager.addTask(new Epic("epic1",Status.NEW,"descr2"));
        fileBackedTaskManager.addTask(new SubTask("sub1",Status.NEW,"descr3",2));
    }

    private void save() {
        List<Task> allTasks = getHistory();
        allTasks.addAll(getAllTasks());
        allTasks.addAll(getAllEpics());
        allTasks.addAll(getAllSubs());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Data"))) {
            for (Task task : allTasks) {
                writer.write(toString(task));
                writer.write("\n");
            }

        } catch (IOException e) {

        }

    }

    @Override
    public Task addTask(Task task) {
        super.addTask(task);
        save();
        return task;
    }

    public String toString(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(task.getId()).append(",");
        sb.append(task.getClass().getSimpleName()).append(",");
        sb.append(task.getName()).append(",");
        sb.append(task.getStatus()).append(",");
        sb.append(task.getDescription());

        // Если объект task является подклассом SubTask, добавляем специфичное поле
        if (task instanceof SubTask) {
            SubTask subTask = (SubTask) task;
            sb.append(",").append(subTask.getEpicId());
        }

        return sb.toString();
    }
    }

