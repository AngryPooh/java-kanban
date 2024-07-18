package manager;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node<Task>> historyMap = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;


    List<Task> getTasks() {
        List<Task> historyList = new ArrayList<>();
        Node<Task> node = head;
        while (node != null) {
            historyList.add(node.elem);
            node = node.next;
        }
        return historyList;
    }

    void linkLast(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, task, null);
        if (head == null) {
            head = newNode;
        } else {
            tail.setNext(newNode);
        }
        tail = newNode;
    }

    void removeNode(int id) {
        final Node<Task> node = historyMap.remove(id);
        if (node == null) {
            return;
        }

        final Node<Task> next = node.getNext();
        final Node<Task> prev = node.getPrev();
        if (prev == null) {
            head = next;
        } else {
            prev.setNext(next);
            node.setPrev(null);
        }
        if (next == null) {
            tail = prev;
        } else {
            next.setPrev(prev);
            node.setNext(null);
        }
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        removeNode(task.getId());
        linkLast(task);
        historyMap.put(task.getId(), tail);
    }

    @Override
    public void remove(int id) {
        removeNode(id);
    }


    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

}