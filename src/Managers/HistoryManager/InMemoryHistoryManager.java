package Managers.HistoryManager;

import Tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int COUNT_OF_TASKS = 10;
    private static final int FIRST_TASK = 0;
    private static class Node{
        private Node next; //хвост(последняя)
        private Node prev; //голова(первая)
        private final Task task; //центр

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
    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private Node last;
    private Node first;

    @Override
    public void add(Task task) {
        Node node = nodeMap.remove(task.getId());
        removeNode(node);
        Node newNode = linkLast(task);
        nodeMap.put(task.getId(), newNode);
    }

    private Node linkLast(Task task) {
        final Node oldNode = this.last;
        final Node newNode = new Node(null, oldNode, task);
        this.last = newNode;
        if (oldNode == null){
            this.first = newNode;
        }else {
            last.prev = oldNode;
            oldNode.next = newNode;
        }
        return newNode;
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        for(Node i = last; i != null; i = i.prev) {
            tasks.add(i.task);
        }
        return tasks;
    }
    private void removeNode(Node node){
        if(node != null) {
            if(node.prev == null) {
                first  = node.next;
                if (node.next != null){
                    node.next.prev = null; //ошибка
                }else{
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
}
