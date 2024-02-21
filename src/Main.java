import manager.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        TaskManager taskManager = new TaskManager();
        Task task = taskManager.addTask(new Task("Таск1", Status.IN_PROGRESS, "описание"));
        Epic epic = taskManager.addEpic(new Epic("Эпик1", Status.IN_PROGRESS, "описание"));
        taskManager.addSub(new SubTask("Саб1", Status.DONE, "описание1", epic.getId()));
        taskManager.addSub(new SubTask("Саб2", Status.DONE, "описание2", epic.getId()));
        taskManager.addSub(new SubTask("Саб3", Status.DONE, "описание3", epic.getId()));
        taskManager.addSub(new SubTask("Саб4", Status.DONE, "описание4", epic.getId()));
        Epic epic2 = taskManager.addEpic(new Epic("Эпик2", Status.NEW, "описание1"));
        //Epic epic1 = taskManager.updateEpic(new Epic("Обновил", Status.NEW, "ds", epic.getId()));
        taskManager.addSub(new SubTask("Саб1", Status.NEW, "описание1", epic2.getId()));
        taskManager.addSub(new SubTask("Саб2", Status.NEW, "описание2", epic2.getId()));
        taskManager.addSub(new SubTask("Саб3", Status.NEW, "описание3", epic2.getId()));
        //Task task1 = taskManager.updateTask(new Task("Таск2", Status.DONE, "описание1", task.getId()));
        //taskManager.removeTasks();
        //taskManager.removeEpics();
        //taskManager.removeSubs();
        //taskManager.deleteEpic(epic1.getId());
        //taskManager.deleteSub(8); //проверил методы, некоторые дейстивтельно некорректно работали, вроде все подправил,спасибо)
        //taskManager.updateSub(new SubTask("обновил", Status.IN_PROGRESS, "sds", epic2.getId(), 9));
        //System.out.println(taskManager.getAllTasks());
        //taskManager.deleteSub(3);
        System.out.println(taskManager.getAllEpics());
        taskManager.deleteEpic(2);
        //taskManager.deleteSub(3);
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubs());
        //System.out.println(taskManager.getSubsFromEpic(2));


        //System.out.println(taskManager.getAllSubs());
        //System.out.println(taskManager.getAllEpics());
        //taskManager.getSubsFromEpic(epic);
        //System.out.println(taskManager.getSubsFromEpic(epic.getId()));
    }
}