package manager;


import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    public static void main(String[] args) throws ManagerSaveException { //Реализовал сценарий, все работаеи
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(new File("Data"));
        fileBackedTaskManager.addTask(new Task("name1", Status.NEW, "desr1"));
        fileBackedTaskManager.addTask(new Epic("epic1", Status.NEW, "descr2"));
        fileBackedTaskManager.addTask(new SubTask("sub1", Status.NEW, "descr3", 2));
        fileBackedTaskManager.getTask(1);
        fileBackedTaskManager.getTask(3);
        FileBackedTaskManager fileBackedTaskManager1 = loadFromFile(new File("Data"));
        System.out.println(fileBackedTaskManager1.getAllSubs());
        System.out.println(fileBackedTaskManager1.getAllTasks());
        System.out.println(fileBackedTaskManager1.getAllEpics());
    }

    private File dataFile;
    private Path dataFilePath;

    public FileBackedTaskManager(File dataFile) {
        this.dataFile = dataFile;
        this.dataFilePath = Path.of(dataFile.getPath());
    }

    public File getDataFile() {
        return dataFile;
    }

    public static FileBackedTaskManager loadFromFile(File file) throws ManagerSaveException {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                Task task = FormatManager.fromString(line);
                if (task != null) {
                    if (task instanceof SubTask) {
                        fileBackedTaskManager.addSub((SubTask) task);
                    } else if (task instanceof Epic) {
                        fileBackedTaskManager.addEpic((Epic) task);
                    } else {
                        fileBackedTaskManager.addTask(task);
                    }
                } else {
                    if (!line.isEmpty()) {
                        List<Integer> list = FormatManager.historyFromString(line);
                        for (int id : list) {
                            Task existingTask = fileBackedTaskManager.getTask(id);
                            SubTask existingSubTask = fileBackedTaskManager.getSub(id);
                            Epic existingEpic = fileBackedTaskManager.getEpic(id);
                            if (existingTask != null) {
                                fileBackedTaskManager.getHistoryManager().add(existingTask);
                            } else if (existingSubTask != null) {
                                fileBackedTaskManager.getHistoryManager().add(existingSubTask);
                            } else if (existingEpic != null) {
                                fileBackedTaskManager.getHistoryManager().add(existingEpic);
                            }
                        }
                    }
                }

            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения", e);
        }
        return fileBackedTaskManager;
    }

    private void save() throws ManagerSaveException {
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(getAllTasks());
        allTasks.addAll(getAllEpics());
        allTasks.addAll(getAllSubs());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile))) {
            writer.append("id,type,name,status,description,epic\n");
            for (Task task : allTasks) {
                writer.write(FormatManager.toString(task));
                writer.write("\n");
            }
            writer.write(FormatManager.historyToString(super.getHistoryManager()));

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения", e);
        }

    }

    @Override
    public void deleteSub(int id) {
        super.deleteSub(id);
        save();
    }

    @Override
    public SubTask getSub(int id) {
        SubTask subTask = super.getSub(id);
        save();
        return subTask;
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }
    @Override
    public void removeTasks() {
        super.removeTasks();
        save();
    }

    @Override
    public void removeEpics() {
        super.removeEpics();
        save();
    }

    @Override
    public void removeSubs() {
        super.removeSubs();
        save();
    }

    @Override
    public Task updateTask(Task task) {
        super.updateTask(task);
        save();
        return task;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
        return epic;
    }

    @Override
    public SubTask updateSub(SubTask subTask) {
        super.updateSub(subTask);
        save();
        return subTask;
    }

    @Override
    public Epic addEpic(Epic epic) {
        super.addEpic(epic);
        save();
        return epic;
    }

    @Override
    public SubTask addSub(SubTask subTask) {
        super.addSub(subTask);
        save();
        return subTask;
    }

    @Override
    public Task addTask(Task task) {
        super.addTask(task);
        save();
        return task;
    }

}

