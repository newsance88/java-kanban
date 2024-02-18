public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        TaskManager taskManager = new TaskManager();
        Task task = taskManager.addTask(new Task("Таск1",Status.IN_PROGRESS));
        Epic epic = taskManager.addEpic(new Epic("Эпик1", Status.IN_PROGRESS));
        taskManager.addSub(new Sub("Саб1", Status.NEW, epic.getId()));
        taskManager.addSub(new Sub("Саб2", Status.NEW, epic.getId()));
        taskManager.addSub(new Sub("Саб3", Status.NEW, epic.getId()));
        taskManager.addSub(new Sub("Саб4", Status.NEW, epic.getId()));
        Epic epic2 = taskManager.addEpic(new Epic("Эпик1", Status.NEW));
        taskManager.addSub(new Sub("Саб1", Status.NEW, epic2.getId()));
        taskManager.addSub(new Sub("Саб2", Status.NEW, epic2.getId()));
        taskManager.addSub(new Sub("Саб3", Status.NEW, epic2.getId()));
        Task task1 = taskManager.updateTask(new Task("Таск2",Status.DONE,task.getId()));
        //taskManager.removeTasks();
        taskManager.test();

    }
}