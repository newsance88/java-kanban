package manager;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    public int id;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, SubTask> subs = new HashMap<>();


    public void getSubsFromEpic(Epic epic) {
        if (!epic.subTaskId.isEmpty()) {
            ArrayList<Integer> subList = epic.subTaskId;
            System.out.println(subList);
        } else {
            System.out.println("Список пуст");
        }
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
        epics.remove(id);
        Epic epic = epics.get(id);
        epic.subTaskId.clear();
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) return;

        ArrayList<Integer> subTaskIds = epic.subTaskId;
        if (subTaskIds == null || subTaskIds.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean allDone = true;
        boolean anyInProgress = false;

        for (Integer subTaskId : subTaskIds) {
            SubTask subTask = subs.get(subTaskId);
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
    }

    public Task updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Неверно введен ID");
        }
        return task;
    }

    public Task updateEpic(Epic epic) {
        if (tasks.containsKey(epic.getId())) {
            tasks.put(epic.getId(), epic);
        } else {
            System.out.println("Неверно введен ID");
        }
        return epic;
    }

    public Task updateSub(SubTask subTask) {
        if (tasks.containsKey(subTask.getId())) {
            tasks.put(subTask.getId(), subTask);
            Epic epic = epics.get(subTask.getEpicId());
            updateEpicStatus(epic.getId());
        } else {
            System.out.println("Неверно введен ID");
        }
        return subTask;
    }

    public Epic addEpic(Epic epic) {
        id++;
        epic.setId(id);
        epics.put(epic.getId(), epic);
        epic.subTaskId = new ArrayList<>();
        updateEpicStatus(epic.getId());
        return epic;
    }

    public SubTask addSub(SubTask subTask) {
        if (epics.containsKey(subTask.getEpicId())) {
            id++;
            subTask.setId(id);
            subs.put(id, subTask);
            Epic epic = epics.get(subTask.getEpicId());
            epic.subTaskId.add(id);
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

    public void printAll() { //забыл переименовать метод test в printAll
        if (tasks.isEmpty() && epics.isEmpty() && subs.isEmpty()) {
            System.out.println("Список задач пуст.");
            return;
        }
        System.out.println("Задачи");
        for (Integer number : tasks.keySet()) {
            System.out.println(tasks.get(number));
        }

        for (Epic epic : epics.values()) {
            System.out.println("Эпик " + epic);
            ArrayList<Integer> subTaskIds = epic.subTaskId;
            if (subTaskIds.isEmpty()) {
                System.out.println("  Подзадачи отсутствуют");
            } else {
                System.out.println("  Подзадачи:");
                for (Integer subTaskId : subTaskIds) {
                    SubTask subTask = subs.get(subTaskId);
                    if (subTask != null) {
                        System.out.println("    " + subTask);
                    }
                }
            }
        }


    }
}
