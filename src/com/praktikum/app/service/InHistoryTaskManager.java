package com.praktikum.app.service;

import com.praktikum.app.models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InHistoryTaskManager implements HistoryManager {

    private List<Task> history = new ArrayList<>();
    private final Map<Integer, Node> customLinkedList = new HashMap<>();
    private Node firstNode;
    private Node lastNode;

    private static class Node {
        private Node previous;
        private Node next;
        private Task value;

        Node(Task value, Node previous, Node next) {
            this.value = value;
            this.previous = previous;
            this.next = next;
        }
    }

    /**
     * Добавляем задачу в customLinkedList,
     * если она уже существует, вырезаем её перенаправляя указатели на новые значения в методе removeNode()
     * и удаляем по ключу из HashMap
     */
    @Override
    public void add(Task task) {
        if (customLinkedList.containsKey(task.getId())) {
            removeNode(customLinkedList.get(task.getId()));
            customLinkedList.remove(task.getId());
        }
        customLinkedList.put(task.getId(), linkLast(task));
    }

    /**
     * @param node узел связного спика, на входе всегда элемент Node из customLinkedList.
     *             переопеределяем ссылки на новые адреса, соседних объектов двусвязного списка
     */
    public void removeNode(Node node) {
        if (node == null) {
            System.out.println("null нельзя удалить");
           return;
        }
        if (node == firstNode) {
            if (firstNode.next != null)
                firstNode = firstNode.next;
            else
                firstNode = null;
        }

        if (node == lastNode) {
            if (lastNode.previous != null)
                lastNode = lastNode.previous;
            else
                lastNode = null;
        }

        if (node.previous != null) {
            node.previous.next = node.next;
            if (node.next != null) {
                node.next.previous = node.previous;
            }
        }
    }

    /**
     * @return Все значение связного списка собираем в List
     */
    public List<Task> getTasks() {
        List<Task> history = new ArrayList<>();
        Node node = firstNode;
        while (node != null) {
            history.add(node.value);
            node = node.next;
        }
        return history;
    }

    /**
     * @return получаем список из нашего связного списка и сохраняем его  в истории, затем возвращаем её копию
     */
    public List<Task> getHistory() {
        history = getTasks();
        return new ArrayList<>(history);
    }

    /**
     * если Map содержит такой ключ вырезаем ноду и удалем пару в мапе
     */
    public void remove(int id) {
        if (customLinkedList.containsKey(id)) {
            removeNode(customLinkedList.get(id));
            customLinkedList.remove(id);
        }
    }

    /**
     * переопределяем ссылки последнего объекта двусвязного списка на новый созданный объект с полученной задачей
     */
    public Node linkLast(Task task) {
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
