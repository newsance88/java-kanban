package manager;

import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private int id;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, SubTask> subs = new HashMap<>();

    private HistoryManager historyManager = Managers.getDefaultHistory();

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public ArrayList<SubTask> getSubsFromEpic(int id) throws ManagerSaveException {
        Epic epic = epics.get(id);
        ArrayList<SubTask> subList = new ArrayList<>();
        for (int number : epic.getSubTaskId()) {
            subList.add(subs.get(number));
        }
        return subList;
    }

    @Override
    public void deleteSub(int id) throws ManagerSaveException {
        SubTask subTask = subs.get(id);
        if (subTask == null) {
            return;
        }

        Epic epic = epics.get(subTask.getEpicId());
        if (epic == null) {
            return;
        }

        ArrayList<Integer> subList = epic.getSubTaskId();
        subList.remove(Integer.valueOf(id));
        epic.setSubTaskId(subList);

        subs.remove(id);
        historyManager.remove(id);
        updateEpicStatus(epic.getId());
    }

    @Override
    public SubTask getSub(int id) throws ManagerSaveException {
        historyManager.add(subs.get(id));
        return subs.get(id);
    }

    @Override
    public void deleteTask(int id) throws ManagerSaveException {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public Task getTask(int id) throws ManagerSaveException {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public void deleteEpic(int id) throws ManagerSaveException {
        Epic epic = epics.get(id);
        if (epic == null) {
            return;
        }
        ArrayList<Integer> subTaskKeysToDelete = epic.getSubTaskId();
        for (int number : subTaskKeysToDelete) {
            subs.remove(number);
            historyManager.remove(number);
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public Epic getEpic(int id) throws ManagerSaveException {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void updateEpicStatus(int epicId) throws ManagerSaveException {
        Epic epic = epics.get(epicId);
        if (epic == null) return;

        ArrayList<Integer> subTaskIds = epic.getSubTaskId();
        if (subTaskIds == null || subTaskIds.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean allDone = true;
        boolean anyInProgress = false;

        for (Integer subTaskId : subTaskIds) {
            SubTask subTask = subs.get(subTaskId);
            if (subTask == null) {
                continue;
            }
            if (subTask.getStatus() != Status.DONE) {
                allDone = false;
            }
            if (subTask.getStatus() == Status.IN_PROGRESS) {
                anyInProgress = true;
            }
        }

        if (allDone) {
            epic.setStatus(Status.DONE);
        } else if (anyInProgress) {
            epic.setStatus(Status.IN_PROGRESS);
        } else {
            epic.setStatus(Status.NEW);
        }
    }

    @Override
    public void removeTasks() throws ManagerSaveException {
        for (int taskId : tasks.keySet()) {
            historyManager.remove(taskId);
        }
        tasks.clear();
    }

    @Override
    public void removeEpics() throws ManagerSaveException {
        for (int taskId : epics.keySet()) {
            historyManager.remove(taskId);
        }
        for (int taskId : subs.keySet()) {
            historyManager.remove(taskId);
        }
        epics.clear();
        subs.clear();
    }

    @Override
    public void removeSubs() throws ManagerSaveException {
        for (int taskId : subs.keySet()) {
            historyManager.remove(taskId);
        }
        subs.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubTaskId();
        }
    }

    @Override
    public Task updateTask(Task task) throws ManagerSaveException {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Неверно введен ID");
        }
        return task;
    }

    @Override
    public Epic updateEpic(Epic epic) throws ManagerSaveException {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            updateEpicStatus(epic.getId());
        } else {
            System.out.println("Неверно введен ID");
        }
        return epic;
    }

    @Override
    public SubTask updateSub(SubTask subTask) throws ManagerSaveException {
        if (subs.containsKey(subTask.getId())) {
            subs.put(subTask.getId(), subTask);
        } else {
            System.out.println("Неверно введен ID");
        }
        Epic epic = epics.get(subTask.getEpicId());
        updateEpicStatus(epic.getId());
        return subTask;
    }

    @Override
    public Epic addEpic(Epic epic) throws ManagerSaveException {
        id++;
        epic.setId(id);
        epics.put(epic.getId(), epic);
        epic.setSubTaskId(new ArrayList<>());
        updateEpicStatus(epic.getId());
        return epic;
    }

    @Override
    public SubTask addSub(SubTask subTask) throws ManagerSaveException {
        if (epics.containsKey(subTask.getEpicId())) {
            id++;
            subTask.setId(id);
            subs.put(id, subTask);
            Epic epic = epics.get(subTask.getEpicId());
            epic.getSubTaskId().add(id);
            updateEpicStatus(epic.getId());
        } else {
            System.out.println("Такого айди эпика нет");
        }

        return subTask;
    }

    @Override
    public Task addTask(Task task) throws ManagerSaveException {
        id++;
        task.setId(id);
        tasks.put(id, task);
        return task;
    }

    @Override
    public ArrayList<Epic> getAllEpics() throws ManagerSaveException {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubs() throws ManagerSaveException {
        return new ArrayList<>(subs.values());
    }

    @Override
    public ArrayList<Task> getAllTasks() throws ManagerSaveException {
        return new ArrayList<>(tasks.values());
    }

}
