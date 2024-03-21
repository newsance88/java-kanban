package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private static class Node {
        private Task data;
        private Node next;
        private Node prev;

        public Node(Task data, Node next, Node prev) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }
    private Map<Integer, Node> historyMap;
    private Node head;
    private Node tail;

    public InMemoryHistoryManager() {
        historyMap = new HashMap<>();
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node node = head;
        while (node != null) {
            tasks.add(node.data);
            node = node.next;
        }
        return tasks;
    }

    private void linkLast(Task task) {
        Node node = new Node(task, null, null);

        if (historyMap.containsKey(task.getId())) {
            remove(task.getId());
        }
        if (head == null) {
            head = node;
        } else {
            tail.next = node;
            node.prev = tail;
        }
        tail = node;
        historyMap.put(task.getId(), node);
    }
    private void removeNode(int id) {
        Node nodeToRemove = historyMap.remove(id);
        if (nodeToRemove == null) {
            return;
        }
        if (nodeToRemove.prev != null) {
            nodeToRemove.prev.next = nodeToRemove.next;
        } else {
            head = nodeToRemove.next;
        }

        if (nodeToRemove.next != null) {
            nodeToRemove.next.prev = nodeToRemove.prev;
        } else {
            tail = nodeToRemove.prev;
        }
    }

    @Override
    public void remove(int id) {
        removeNode(id);
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
        return getTasks();
    }
}
