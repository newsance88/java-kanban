package manager;

import exceptions.TasksCrossException;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private int id;
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, SubTask> subs = new HashMap<>();
    protected TreeSet<Task> prioritizedTasks = new TreeSet<>((task1, task2) ->
            Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(Task::getId)
                    .compare(task1, task2));

    protected HistoryManager historyManager = Managers.getDefaultHistory();

    private boolean ifTasksCross(Task task1, Task task2) {
        if (task1.getStartTime() == null || task1.getEndTime() == null ||
                task2.getStartTime() == null || task2.getEndTime() == null) {
            return false;
        }
        return !(task1.getStartTime().isAfter(task2.getEndTime()) || task1.getEndTime().isBefore(task2.getStartTime()));
    }

    @Override
    public ArrayList<Task> getPrioritizedTasks() {
        prioritizedTasks.clear();
        prioritizedTasks.addAll(tasks.values().stream()
                .filter(task -> task.getStartTime() != null && task.getDuration() != null)
                .toList());

        prioritizedTasks.addAll(subs.values().stream()
                .filter(subTask -> subTask.getStartTime() != null && subTask.getDuration() != null)
                .toList());

        return new ArrayList<>(prioritizedTasks);
    }


    private void updateEpicTime(Epic epic) {
        LocalDateTime endTime = LocalDateTime.of(1000, 1, 1, 1, 1);
        long duration = 0;
        LocalDateTime startTime = LocalDateTime.of(3000, 12, 21, 21, 12);
        ArrayList<Integer> list = epic.getSubTaskId();
        if (list.isEmpty()) {
            epic.setDuration(null);
            epic.setEndTime(null);
            epic.setStartTime(null);
            return;
        }
        for (int number : list) {
            SubTask sub = subs.get(number);
            if ((sub.getStartTime() == null) || (sub.getEndTime() == null) || (sub.getDuration() == null)) {
                continue;
            }
            if (sub.getStartTime().isBefore(startTime)) {
                startTime = sub.getStartTime();
            }
            duration = duration + sub.getDuration().toMinutes();
            if (sub.getEndTime().isAfter(endTime) && !(sub.getEndTime() == null)) {
                endTime = sub.getEndTime();
            }
        }
        epic.setEndTime(endTime);
        epic.setStartTime(startTime);
        epic.setDuration(Duration.ofMinutes(duration));

    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public ArrayList<SubTask> getSubsFromEpic(int id) {
        Epic epic = epics.get(id);
        return epic.getSubTaskId().stream()
                .map(subs::get)
                .collect(Collectors.toCollection(ArrayList::new));
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
        updateEpicTime(epic);

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
        epic.getSubTaskId().stream()
                .forEach(subTaskId -> {
                    subs.remove(subTaskId);
                    historyManager.remove(subTaskId);
                });
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
        tasks.keySet().forEach(historyManager::remove);
        tasks.clear();
    }


    @Override
    public void removeEpics() {
        epics.keySet().stream().forEach(historyManager::remove);
        subs.keySet().stream().forEach(historyManager::remove);
        epics.clear();
        subs.clear();
    }

    @Override
    public void removeSubs() {
        subs.keySet().stream().forEach(historyManager::remove);
        subs.clear();
        epics.values().forEach(Epic::clearSubTaskId);
        for (Epic epic : epics.values()) {
            updateEpicTime(epic);
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
        updateEpicTime(epic);
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
            List<Task> list = getPrioritizedTasks().stream()
                    .filter(subtask1 -> ifTasksCross(subtask1, subTask))
                    .toList();
            if (!list.isEmpty()) {
                System.out.println("Задача пересекается");
                throw new TasksCrossException("Задача пересекается");
            }
            id++;
            subTask.setId(id);
            subs.put(id, subTask);
            Epic epic = epics.get(subTask.getEpicId());
            epic.getSubTaskId().add(id);
            updateEpicStatus(epic.getId());
            updateEpicTime(epic);
        } else {
            System.out.println("Такого айди эпика нет");
        }

        return subTask;
    }

    @Override
    public Task addTask(Task task) {
        List<Task> list = getPrioritizedTasks().stream()
                .filter(task1 -> ifTasksCross(task1, task))
                .toList();
        if (!list.isEmpty()) {
            System.out.println("Задача пересекается");
            throw new TasksCrossException("Задача пересекается");

        }
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
