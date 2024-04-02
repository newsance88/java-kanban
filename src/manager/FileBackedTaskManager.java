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
                Task task = fromString(line);
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
                        List<Integer> list = historyFromString(line);
                        for (int id : list) {
                            // Добавляем в историю только существующие задачи, эпики и подзадачи
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
                writer.write(toString(task));
                writer.write("\n");
            }
            writer.write(historyToString(super.getHistoryManager()));

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения", e);
        }

    }

    public String historyToString(HistoryManager manager) {
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
                if (taskValues[1].equals("Task")) {
                    Task task = new Task("", Status.NEW, "");
                    task.setId(Integer.parseInt(taskValues[0]));
                    task.setName(taskValues[2]);
                    task.setStatus(Status.valueOf(taskValues[3]));
                    task.setDescription(taskValues[4]);
                    return task;
                } else if (taskValues[1].equals("Epic")) {
                    Epic epic = new Epic("", Status.NEW, "");
                    epic.setId(Integer.parseInt(taskValues[0]));
                    epic.setName(taskValues[2]);
                    epic.setStatus(Status.valueOf(taskValues[3]));
                    epic.setDescription(taskValues[4]);
                    return epic;
                } else if (taskValues[1].equals("SubTask")) {
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

    public String toString(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(task.getId()).append(",");
        sb.append(task.getClass().getSimpleName()).append(",");
        sb.append(task.getName()).append(",");
        sb.append(task.getStatus()).append(",");
        sb.append(task.getDescription());

        if (task instanceof SubTask) {
            SubTask subTask = (SubTask) task;
            sb.append(",").append(subTask.getEpicId());
        }

        return sb.toString();
    }

    @Override
    public ArrayList<SubTask> getSubsFromEpic(int id) throws ManagerSaveException {
        ArrayList<SubTask> list = super.getSubsFromEpic(id);
        save();
        return list;
    }

    @Override
    public void deleteSub(int id) throws ManagerSaveException {
        super.deleteSub(id);
        save();
    }

    @Override
    public SubTask getSub(int id) throws ManagerSaveException {
        SubTask subTask = super.getSub(id);
        save();
        return subTask;
    }

    @Override
    public void deleteTask(int id) throws ManagerSaveException {
        super.deleteTask(id);
        save();
    }

    @Override
    public Task getTask(int id) throws ManagerSaveException {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public void deleteEpic(int id) throws ManagerSaveException {
        super.deleteEpic(id);
        save();
    }

    @Override
    public Epic getEpic(int id) throws ManagerSaveException {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public void updateEpicStatus(int epicId) throws ManagerSaveException {
        super.updateEpicStatus(epicId);
        save();
    }

    @Override
    public void removeTasks() throws ManagerSaveException {
        super.removeTasks();
        save();
    }

    @Override
    public void removeEpics() throws ManagerSaveException {
        super.removeEpics();
        save();
    }

    @Override
    public void removeSubs() throws ManagerSaveException {
        super.removeSubs();
        save();
    }

    @Override
    public Task updateTask(Task task) throws ManagerSaveException {
        super.updateTask(task);
        save();
        return task;
    }

    @Override
    public Epic updateEpic(Epic epic) throws ManagerSaveException {
        super.updateEpic(epic);
        save();
        return epic;
    }

    @Override
    public SubTask updateSub(SubTask subTask) throws ManagerSaveException {
        super.updateSub(subTask);
        save();
        return subTask;
    }

    @Override
    public Epic addEpic(Epic epic) throws ManagerSaveException {
        super.addEpic(epic);
        save();
        return epic;
    }

    @Override
    public SubTask addSub(SubTask subTask) throws ManagerSaveException {
        super.addSub(subTask);
        save();
        return subTask;
    }

    @Override
    public Task addTask(Task task) throws ManagerSaveException {
        super.addTask(task);
        save();
        return task;
    }

}

