package com.praktikum.app;

import com.praktikum.app.models.Epic;
import com.praktikum.app.models.Subtask;
import com.praktikum.app.models.Task;
import com.praktikum.app.models.utils.Status;
import com.praktikum.app.service.Manager;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();

        System.out.println("\n------------Созадаём задачи--------------");
        manager.createTask(new Task("Задача-1", "Описание задачи-1", Status.NEW));
        manager.createTask(new Task("Задача-2", "Описание задачи-2", Status.NEW));
        manager.createEpicTask(new Epic("Эпик-1", "Описание эпика-1"));
        manager.createEpicTask(new Epic("Эпик-2", "Описание эпика-2"));
        manager.createSubTask(new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, 3));
        manager.createSubTask(new Subtask("Подзадача-2", "Описание подзадачи-2", Status.NEW, 3));
        manager.createSubTask(new Subtask("Подзадача-3", "Описание подзадачи-3", Status.NEW, 4));

        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());

        manager.updateTask(new Task("Задача-1", "Обновлённое описание задачи-1", Status.IN_PROGRESS, 1));
        manager.updateTask(new Task("Задача-2", "Обновлённое описание задачи-2", Status.DONE, 2));
        manager.updateSubtask(new Subtask("Подзадача-1", "Описание обновлённой подзадачи-1", Status.IN_PROGRESS, 3, 5));
        manager.updateSubtask(new Subtask("Подзадача-2", "Описание обновлённой подзадачи-2", Status.IN_PROGRESS, 3,6));
        manager.updateSubtask(new Subtask("Подзадача-3", "Описание обновлённой подзадачи-3", Status.DONE, 4,7));

        System.out.println("\n---------Задачи после обновления---------");
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());

        System.out.println("\n---------Обновляем эпики--------");
        manager.updateEpic(new Epic("Эпик-1", "Описание обновлённого эпика-1", 3));
        manager.updateEpic(new Epic("Эпик-2", "Описание обновлённого эпика-2", 4));
        System.out.println(manager.getEpics());

        manager.deleteTaskById(1);
        manager.deleteEpicById(3);
        System.out.println("\n---------Задачи после удаления---------");
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());
    }
}
