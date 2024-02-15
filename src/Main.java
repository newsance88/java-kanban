public class Main {

    public static void main(String[] args) {
        Task task = new Task("name", "discr", Status.DONE);
        TaskManager taskManager = new TaskManager();
        System.out.println(taskManager.addTask(task));
        System.out.println("ХАХАХААХАХ");
    }
}
