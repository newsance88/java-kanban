package manager;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    public int id;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, SubTask> subs = new HashMap<>();


    public ArrayList<SubTask> getSubsFromEpic(int id) {
        Epic epic = epics.get(id);
        ArrayList<Integer> idList = epic.getSubTaskId();
        ArrayList<SubTask> subList = new ArrayList<>();
        for (int number : idList) {
            subList.add(subs.get(number));
        }
        return subList;
    }

    public void deleteSub(int id) {
        SubTask subTask = subs.get(id);
        Epic epic = epics.get(subTask.getEpicId());
        updateEpicStatus(epic.getId());
        subs.remove(id);
    }

    public SubTask getSub(int id) {
        return subs.get(id);
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public void deleteEpic(int id) {
        ArrayList<Integer> subTaskKeysToDelete = new ArrayList<>();
        for (SubTask subTask : subs.values()) {
            if (subTask.getEpicId() == id) {
                subTaskKeysToDelete.add(subTask.getId());
            }
        }
        for (int number : subTaskKeysToDelete) {
            subs.remove(number);
        }
        epics.remove(id);
    }

    public Epic getEpic(int id) {
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

    public void removeTasks() {
        tasks.clear();
    }

    public void removeEpics() {
        epics.clear();
    }

    public void removeSubs() {
        subs.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubTaskId();
        }
    }

    public Task updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Неверно введен ID");
        }
        return task;
    }

    public Epic updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            updateEpicStatus(epic.getId());
        } else {
            System.out.println("Неверно введен ID");
        }
        return epic;
    }

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

    public Epic addEpic(Epic epic) {
        id++;
        epic.setId(id);
        epics.put(epic.getId(), epic);
        epic.setSubTaskId(new ArrayList<>());
        updateEpicStatus(epic.getId());
        return epic;
    }

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

    public Task addTask(Task task) {
        id++;
        task.setId(id);
        tasks.put(id, task);
        return task;
    }

    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> epicList = new ArrayList<>();
        for (Epic epic : epics.values()) {
            epicList.add(epic);
        }
        return epicList;
    }

    public ArrayList<SubTask> getAllSubs() {
        ArrayList<SubTask> subList = new ArrayList<>();
        for (SubTask subTask : subs.values()) {
            subList.add(subTask);
        }
        return subList;
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> taskList = new ArrayList<>();
        for (Task task : tasks.values()) {
            taskList.add(task);
        }
        return taskList;
    }

}
