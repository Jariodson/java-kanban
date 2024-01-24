package Managers.HistoryManager;

import Tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private Node last;
    private Node first;

    @Override
    public void add(Task task) {
        if (task == null) {
            throw new NullPointerException("Пустая задача.");
        }
        Node node = nodeMap.remove(task.getId());
        removeNode(node);
        Node newNode = linkLast(task);
        nodeMap.put(task.getId(), newNode);
    }

    private Node linkLast(Task task) {
        final Node oldNode = this.last;
        final Node newNode = new Node(null, oldNode, task);
        this.last = newNode;
        if (oldNode == null) {
            this.first = newNode;
        } else {
            oldNode.next = newNode;
        }
        return newNode;
    }

    private List<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        for (Node i = last; i != null; i = i.prev) {
            tasks.add(i.task);
        }
        return tasks;
    }

    private void removeNode(Node node) {
        if (node != null) {
            if (node.prev == null) {
                first = node.next;
                if (node.next != null) {
                    node.next.prev = null;
                } else {
                    last = null;
                }
            } else if (node.next != null) {
                node.next.prev = node.prev;
                node.prev.next = node.next;
            } else {
                last = node.prev;
                node.prev.next = null;
            }
        }
    }

    @Override
    public void remove(int id) {
        Node node = nodeMap.remove(id);
        removeNode(node);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private static class Node {
        private Node next;
        private Node prev;
        private final Task task;

        @Override
        public String toString() {
            return "Node{" +
                    "task=" + task.getId() +
                    '}';
        }

        public Node(Node next, Node prev, Task task) {
            this.next = next;
            this.prev = prev;
            this.task = task;
        }
    }
}
