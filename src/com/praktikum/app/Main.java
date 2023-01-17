package com.praktikum.app;

import com.praktikum.app.models.Epic;
import com.praktikum.app.models.Subtask;
import com.praktikum.app.models.Task;
import com.praktikum.app.models.utils.Status;
import com.praktikum.app.service.*;

public class Main {
    public static void main(String[] args) {
        TaskManager memoryManager = Managers.getDefault();

        System.out.println("\n------------Созадаём задачи--------------");
        memoryManager.createTask(new Task("Задача-1", "Описание задачи с id 1", Status.NEW));
        memoryManager.createTask(new Task("Задача-2", "Описание задачи с id 2", Status.NEW));
        memoryManager.createEpicTask(new Epic("Эпик-1", "Описание эпика с id 3"));
        memoryManager.createEpicTask(new Epic("Эпик-2", "Описание эпика с id 4"));
        memoryManager.createSubTask(new Subtask("Подзадача-1", "Описание подзадачи с id 5", Status.NEW, 3));
        memoryManager.createSubTask(new Subtask("Подзадача-2", "Описание подзадачи с id 6", Status.NEW, 3));
        memoryManager.createSubTask(new Subtask("Подзадача-3", "Описание подзадачи с id 7", Status.NEW, 4));

        System.out.println(memoryManager.getTasks());
        System.out.println(memoryManager.getEpics());
        System.out.println(memoryManager.getSubtasks());

        memoryManager.updateTask(new Task("Задача-1", "Обновлённое описание задачи", Status.IN_PROGRESS, 1));
        memoryManager.updateTask(new Task("Задача-2", "Обновлённое описание задачи", Status.DONE, 2));
        memoryManager.updateSubtask(new Subtask("Подзадача-1", "Описание обновлённой подзадачи", Status.IN_PROGRESS, 3, 5));
        memoryManager.updateSubtask(new Subtask("Подзадача-2", "Описание обновлённой подзадачи", Status.IN_PROGRESS, 3,6));
        memoryManager.updateSubtask(new Subtask("Подзадача-3", "Описание обновлённой подзадачи", Status.DONE, 4,7));

        System.out.println("\n---------Задачи после обновления---------");
        System.out.println(memoryManager.getTasks());
        System.out.println(memoryManager.getEpics());
        System.out.println(memoryManager.getSubtasks());

        System.out.println("\n---------Обновляем эпики--------");
        memoryManager.updateEpic(new Epic("Эпик-1", "Описание обновлённого эпика", 3));
        memoryManager.updateEpic(new Epic("Эпик-2", "Описание обновлённого эпика", 4));
        System.out.println(memoryManager.getEpics());

//        memoryManager.deleteTaskById(1);
//        memoryManager.deleteEpicById(3);
//        System.out.println("\n---------Задачи после удаления---------");
//        System.out.println(memoryManager.getTasks());
//        System.out.println(memoryManager.getEpics());
//        System.out.println(memoryManager.getSubtasks());

        System.out.println("\n---------Добавляем ещё задач и выводим историю---------");
        memoryManager.createTask(new Task("Задача-3", "Описание задачис id 8", Status.NEW));
        memoryManager.createTask(new Task("Задача-4", "Описание задачи с id 9", Status.NEW));
        memoryManager.createEpicTask(new Epic("Эпик-3", "Описание эпика с id 10"));
        memoryManager.createEpicTask(new Epic("Эпик-4", "Описание эпика с id 11"));
        memoryManager.createSubTask(new Subtask("Подзадача-4", "Описание подзадачи с id 12", Status.NEW, 10));
        memoryManager.createSubTask(new Subtask("Подзадача-5", "Описание подзадачи с id 13", Status.NEW, 10));
        memoryManager.createSubTask(new Subtask("Подзадача-6", "Описание подзадачи с id 14", Status.NEW, 11));



        memoryManager.getTaskById(1);
        memoryManager.getTaskById(2);
        memoryManager.getEpicTaskById(3);
        memoryManager.getEpicTaskById(4);
        memoryManager.getSubTaskById(5);
        memoryManager.getSubTaskById(6);
        memoryManager.getSubTaskById(7);
        memoryManager.getTaskById(8);
        memoryManager.getTaskById(9);
        memoryManager.getEpicTaskById(10);
//        memoryManager.getEpicTaskById(11);


        System.out.println(memoryManager.getHistory());
    }
}
