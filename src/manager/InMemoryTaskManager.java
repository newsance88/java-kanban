package manager;

import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private int id;
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, SubTask> subs = new HashMap<>();

    protected HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public ArrayList<SubTask> getSubsFromEpic(int id) {
        Epic epic = epics.get(id);
        ArrayList<SubTask> subList = new ArrayList<>();
        for (int number : epic.getSubTaskId()) {
            subList.add(subs.get(number));
        }
        return subList;
    }

    @Override
    public void deleteSub(int id) {
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
    public SubTask getSub(int id) {
        historyManager.add(subs.get(id));
        return subs.get(id);
    }

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public Task getTask(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public void deleteEpic(int id) {
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
    public Epic getEpic(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }


    private void updateEpicStatus(int epicId) {
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
    public void removeTasks() {
        for (int taskId : tasks.keySet()) {
            historyManager.remove(taskId);
        }
        tasks.clear();
    }

    @Override
    public void removeEpics() {
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
    public void removeSubs() {
        for (int taskId : subs.keySet()) {
            historyManager.remove(taskId);
        }
        subs.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubTaskId();
        }
    }

    @Override
    public Task updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Неверно введен ID");
        }
        return task;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            updateEpicStatus(epic.getId());
        } else {
            System.out.println("Неверно введен ID");
        }
        return epic;
    }

    @Override
    public SubTask updateSub(SubTask subTask) {
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
    public Epic addEpic(Epic epic) {
        id++;
        epic.setId(id);
        epics.put(epic.getId(), epic);
        epic.setSubTaskId(new ArrayList<>());
        updateEpicStatus(epic.getId());
        return epic;
    }

    @Override
    public SubTask addSub(SubTask subTask) {
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
    public Task addTask(Task task) {
        id++;
        task.setId(id);
        tasks.put(id, task);
        return task;
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubs() {
        return new ArrayList<>(subs.values());
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

}
