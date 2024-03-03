package manager;

import tasks.*;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int HISTORY_LENGTH = 10;
    private ArrayList<Task> historyList;

    public InMemoryHistoryManager() {
        historyList = new ArrayList<>();
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (historyList.size() >= HISTORY_LENGTH) {
            historyList.remove(0);
        }
        historyList.add(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(historyList);
    }
}
