import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private static int id;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Sub> subs = new HashMap<>();
    private HashMap<Integer, ArrayList<Integer>> epicToSub = new HashMap<>();

    public void getSubsFromEpic(Epic epic) {
        if (epicToSub.containsKey(epic.getId())) {
            ArrayList<Integer> subList = epicToSub.get(epic.getId());
            for (Integer number: subList) {
                System.out.println(getSub(number));
            }
        } else {
            System.out.println("Неверно введен ID");
        }
    }

    public void deleteSub(int id) {
        if (subs.containsKey(id)) {
            subs.remove(id);
        } else {
            System.out.println("Неверно введен ID");
        }
    }

    public Sub getSub(int id) {
        if (subs.containsKey(id)) {
            Sub sub = subs.get(id);
            return sub;
        } else {
            System.out.println("Неверно введен ID");
            return null;
        }
    }

    public void deleteTask(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("Неверно введен ID");
        }
    }

    public Task getTask(int id) {
        if (tasks.containsKey(id)) {
            Task task = tasks.get(id);
            return task;
        } else {
            System.out.println("Неверно введен ID");
            return null;
        }
    }

    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            epics.remove(id);
        } else {
            System.out.println("Неверно введен ID");
        }
    }

    public Epic getEpic(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            return epic;
        } else {
            System.out.println("Неверно введен ID");
            return null;
        }
    }

    public void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) return;

        ArrayList<Integer> subTaskIds = epicToSub.get(epicId);
        if (subTaskIds == null || subTaskIds.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean allDone = true;
        boolean anyInProgress = false;

        for (Integer subTaskId : subTaskIds) {
            Sub sub = subs.get(subTaskId);
            if (sub.getStatus() != Status.DONE) {
                allDone = false;
            }
            if (sub.getStatus() == Status.IN_PROGRESS) {
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
        epics.clear();
        subs.clear();
        epicToSub.clear();
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

    public Task updateSub(Sub sub) {
        if (tasks.containsKey(sub.getId())) {
            tasks.put(sub.getId(), sub);
        } else {
            System.out.println("Неверно введен ID");
        }
        return sub;
    }

    public Epic addEpic(Epic epic) {
        id++;
        epic.setId(id);
        epics.put(id, epic);
        epicToSub.put(id, new ArrayList<>());
        updateEpicStatus(epic.getId());
        return epic;
    }

    public Sub addSub(Sub sub) {
        id++;
        sub.setId(id);
        subs.put(id, sub);
        if (epicToSub.containsKey(sub.getEpicId())) {
            ArrayList<Integer> newSubList = epicToSub.get(sub.getEpicId());
            newSubList.add(id);
            epicToSub.put(sub.getEpicId(), newSubList);
            updateEpicStatus(sub.getEpicId());
        } else {
            System.out.println("Такого Айди эпика нету");
        }
        return sub;
    }

    public Task addTask(Task task) {
        id++;
        task.setId(id);
        tasks.put(id, task);
        return task;
    }

    public void test() {
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
            ArrayList<Integer> subTaskIds = epicToSub.getOrDefault(epic.getId(), new ArrayList<>());
            if (subTaskIds.isEmpty()) {
                System.out.println("  Подзадачи отсутствуют");
            } else {
                System.out.println("  Подзадачи:");
                for (Integer subTaskId : subTaskIds) {
                    Sub subTask = subs.get(subTaskId);
                    if (subTask != null) {
                        System.out.println("    " + subTask);
                    }
                }
            }
        }


    }
}
