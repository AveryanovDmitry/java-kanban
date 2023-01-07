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
        manager.createSubTask(new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, 1));
        manager.createSubTask(new Subtask("Подзадача-2", "Описание подзадачи-2", Status.NEW, 1));
        manager.createSubTask(new Subtask("Подзадача-3", "Описание подзадачи-3", Status.NEW, 2));

        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());

        manager.updateTask(new Task("Задача-1", "Обновлённое описание задачи-1", Status.IN_PROGRESS));
        manager.updateTask(new Task("Задача-2", "Обновлённое описание задачи-2", Status.DONE));
        manager.updateEpic(new Epic("Эпик-1", "Описание обновлённого эпика"));
        manager.updateEpic(new Epic("Эпик-2", "Описание обновлённого эпика-2"));
        manager.updateSubTask(new Subtask("Подзадача-1", "Описание обновлённой подзадачи-1", Status.IN_PROGRESS, 1));
        manager.updateSubTask(new Subtask("Подзадача-2", "Описание обновлённой подзадачи-2", Status.IN_PROGRESS, 1));
        manager.updateSubTask(new Subtask("Подзадача-3", "Описание обновлённой подзадачи-3", Status.DONE, 2));

        System.out.println("\n---------Задачи после обновления---------");
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());


        manager.deleteTaskById(1);
        manager.deleteEpicById(1);
        System.out.println("\n---------Задачи после удаления---------");
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());
    }
}
