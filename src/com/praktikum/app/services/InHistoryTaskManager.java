package com.praktikum.app.services;

import com.praktikum.app.models.Task;

import java.util.*;

public class InHistoryTaskManager implements HistoryManager {
    private final Map<Integer, Node> mapHistory = new HashMap<>();
    private Node firstNode;
    private Node lastNode;

    private static class Node {
        private Node previous;
        private Node next;
        private final Task value;

        Node(Task value, Node previous, Node next) {
            this.value = value;
            this.previous = previous;
            this.next = next;
        }
    }

    /**
     * Добавляем задачу в mapHistory,
     * если она уже существует, вырезаем её перенаправляя указатели на новые значения в методе removeNode()
     * и удаляем по ключу из HashMap
     */
    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (mapHistory.containsKey(task.getId())) {
            removeNode(mapHistory.remove(task.getId()));
        }
        mapHistory.put(task.getId(), linkLast(task));
    }

    /**
     * @param node узел связного спика, на входе всегда элемент Node из mapHistory.
     *             переопеределяем ссылки на новые адреса, соседних объектов двусвязного списка
     */
    private void removeNode(Node node) {
        if (node == null) {
            System.out.println("null нельзя удалить");
            return;
        }

        if (node == firstNode) {
            firstNode = firstNode.next;
        }

        if (node == lastNode) {
            lastNode = lastNode.previous;
        }

        if (node.previous != null) {
            node.previous.next = node.next;
        }

        if (node.next != null) {
            node.next.previous = node.previous;
        }
    }

    /**
     * @return Все значение связного списка собираем в List
     */
    private List<Task> getTasks() {
        List<Task> history = new ArrayList<>();
        Node node = firstNode;
        while (node != null) {
            history.add(node.value);
            node = node.next;
        }
        return history;
    }

    /**
     * @return возвращаем список из нашего связного списка
     */
    public List<Task> getHistory() {
        return getTasks();
    }

    /**
     * если Map содержит такой ключ вырезаем ноду и удалем пару в мапе
     */
    public void remove(int id) {
        if (mapHistory.containsKey(id)) {
            removeNode(mapHistory.remove(id));
        }
    }

    /**
     * переопределяем ссылки последнего объекта двусвязного списка на новый созданный объект с полученной задачей
     */
    private Node linkLast(Task task) {
        Node newLastNode = new Node(task, lastNode, null);
        if (lastNode != null) {
            lastNode.next = newLastNode;
        } else {
            firstNode = newLastNode;
        }
        lastNode = newLastNode;
        return lastNode;
    }
}
