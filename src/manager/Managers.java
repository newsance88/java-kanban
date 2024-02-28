package manager;

public class Managers { //Я вот не совсем понимаю зачем нам столько интерфейсов, как будто пока мы только усложняем программу
    public Managers() {

    }
    public TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
    public HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
