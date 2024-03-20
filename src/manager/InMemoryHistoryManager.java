package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private Map<Integer, Node> historyMap;
    private Node<Task> head;
    private Node<Task> tail;

    public InMemoryHistoryManager() {
        historyMap = new HashMap<>();
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node<Task> node = head;
        while (node != null) {
            tasks.add(node.getData());
            node = node.getNext();
        }
        return tasks;
    }

    public void linkLast(Task task) {
        Node<Task> node = new Node<>(task, null, null);

        if (historyMap.containsKey(task.getId())) {
            remove(task.getId());
        }
        if (head == null) {
            head = node;
            tail = node;
        } else {
            tail.setNext(node);
            node.setPrev(tail);
            tail = node;
        }
        historyMap.put(task.getId(), node);
    }

    @Override
    public void remove(int id) {
        Node<Task> nodeToRemove = historyMap.get(id);
        if (nodeToRemove == null) {
            return;
        }
        if (nodeToRemove.getPrev() != null) {
            nodeToRemove.getPrev().setNext(nodeToRemove.getNext());
        } else {
            head = nodeToRemove.getNext();
        }

        if (nodeToRemove.getNext() != null) {
            nodeToRemove.getNext().setPrev(nodeToRemove.getPrev());
        } else {
            tail = nodeToRemove.getPrev();
        }
        historyMap.remove(id);
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        linkLast(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(getTasks());
    }
}
