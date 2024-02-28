import manager.InMemoryTaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task = inMemoryTaskManager.addTask(new Task("Таск1", Status.IN_PROGRESS, "описание"));
        Task task1 = inMemoryTaskManager.addTask(new Task("Таск1", Status.IN_PROGRESS, "описание"));
        Task task2 = inMemoryTaskManager.addTask(new Task("Таск1", Status.IN_PROGRESS, "описание"));
        Task task3 = inMemoryTaskManager.addTask(new Task("Таск1", Status.IN_PROGRESS, "описание"));
        Task task4 = inMemoryTaskManager.addTask(new Task("Таск1", Status.IN_PROGRESS, "описание"));
        Task task5 = inMemoryTaskManager.addTask(new Task("Таск1", Status.IN_PROGRESS, "описание"));
        Task task6 = inMemoryTaskManager.addTask(new Task("Таск1", Status.IN_PROGRESS, "описание"));
        Task task7 = inMemoryTaskManager.addTask(new Task("Таск1", Status.IN_PROGRESS, "описание"));
        Task task8 = inMemoryTaskManager.addTask(new Task("Таск1", Status.IN_PROGRESS, "описание"));
        Task task9 = inMemoryTaskManager.addTask(new Task("Таск1", Status.IN_PROGRESS, "описание"));
        Epic epic = inMemoryTaskManager.addEpic(new Epic("Эпик1", Status.IN_PROGRESS, "описание"));
        inMemoryTaskManager.addSub(new SubTask("Саб1", Status.DONE, "описание1", epic.getId()));
        inMemoryTaskManager.addSub(new SubTask("Саб2", Status.DONE, "описание2", epic.getId()));
        inMemoryTaskManager.addSub(new SubTask("Саб3", Status.DONE, "описание3", epic.getId()));
        inMemoryTaskManager.addSub(new SubTask("Саб4", Status.DONE, "описание4", epic.getId()));
        Epic epic2 = inMemoryTaskManager.addEpic(new Epic("Эпик2", Status.NEW, "описание1"));
        //Epic epic1 = taskManager.updateEpic(new Epic("Обновил", Status.NEW, "ds", epic.getId()));
        inMemoryTaskManager.addSub(new SubTask("Саб1", Status.NEW, "описание1", epic2.getId()));
        inMemoryTaskManager.addSub(new SubTask("Саб2", Status.NEW, "описание2", epic2.getId()));
        inMemoryTaskManager.addSub(new SubTask("Саб3", Status.NEW, "описание3", epic2.getId()));
        //Task task1 = taskManager.updateTask(new Task("Таск2", Status.DONE, "описание1", task.getId()));
        //taskManager.removeTasks();
        //taskManager.removeEpics();
        //taskManager.removeSubs();
        //taskManager.deleteEpic(epic1.getId());
        //taskManager.deleteSub(8); //проверил методы, некоторые дейстивтельно некорректно работали, вроде все подправил,спасибо)
        //taskManager.updateSub(new SubTask("обновил", Status.IN_PROGRESS, "sds", epic2.getId(), 9));
        //System.out.println(taskManager.getAllTasks());
        //taskManager.deleteSub(3);
        //System.out.println(inMemoryTaskManager.getAllEpics());
        //inMemoryTaskManager.deleteEpic(2);
        //taskManager.deleteSub(3);
        //System.out.println(inMemoryTaskManager.getAllEpics());
        inMemoryTaskManager.getTask(task.getId());
        inMemoryTaskManager.getEpic(epic.getId());
        inMemoryTaskManager.getTask(task1.getId());
        inMemoryTaskManager.getTask(task2.getId());
        inMemoryTaskManager.getTask(task3.getId());
        inMemoryTaskManager.getTask(task4.getId());
        inMemoryTaskManager.getTask(task5.getId());
        inMemoryTaskManager.getTask(task6.getId());
        inMemoryTaskManager.getTask(task7.getId());
        inMemoryTaskManager.getTask(task8.getId());
        inMemoryTaskManager.getTask(task9.getId());

        System.out.println(inMemoryTaskManager.getHistory());
        //System.out.println(taskManager.getSubsFromEpic(2));


        //System.out.println(taskManager.getAllSubs());
        //System.out.println(taskManager.getAllEpics());
        //taskManager.getSubsFromEpic(epic);
        //System.out.println(taskManager.getSubsFromEpic(epic.getId()));
    }
}