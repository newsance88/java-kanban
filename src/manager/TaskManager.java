package manager;

import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;

public interface TaskManager {

    ArrayList<Task> getHistory();

    ArrayList<SubTask> getSubsFromEpic(int id) throws ManagerSaveException;

    void deleteSub(int id) throws ManagerSaveException;

    SubTask getSub(int id) throws ManagerSaveException;

    void deleteTask(int id) throws ManagerSaveException;

    Task getTask(int id) throws ManagerSaveException;

    void deleteEpic(int id) throws ManagerSaveException;

    Epic getEpic(int id) throws ManagerSaveException;

    void updateEpicStatus(int epicId) throws ManagerSaveException;

    void removeTasks() throws ManagerSaveException;

    void removeEpics() throws ManagerSaveException;

    void removeSubs() throws ManagerSaveException;

    Task updateTask(Task task) throws ManagerSaveException;

    Epic updateEpic(Epic epic) throws ManagerSaveException;

    SubTask updateSub(SubTask subTask) throws ManagerSaveException;

    Epic addEpic(Epic epic) throws ManagerSaveException;

    SubTask addSub(SubTask subTask) throws ManagerSaveException;

    Task addTask(Task task) throws ManagerSaveException;

    ArrayList<Epic> getAllEpics() throws ManagerSaveException;

    ArrayList<SubTask> getAllSubs() throws ManagerSaveException;

    ArrayList<Task> getAllTasks() throws ManagerSaveException;
}
