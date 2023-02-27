package com.praktikum.app;

import com.praktikum.app.models.Epic;
import com.praktikum.app.models.Subtask;
import com.praktikum.app.models.Task;
import com.praktikum.app.models.utils.Status;
import com.praktikum.app.models.utils.TypeTask;
import com.praktikum.app.services.Managers;
import com.praktikum.app.services.inFileManager.FileBackendTasksManager;
import com.praktikum.app.services.inMemoryManager.TaskManager;

import java.io.File;

public class Main {
    public static void main(String[] args) {

        TaskManager memoryManager = Managers.getDefault();

        /**
         * создайте две задачи, эпик с тремя подзадачами и эпик без подзадач;
         * запросите созданные задачи несколько раз в разном порядке;
         * после каждого запроса выведите историю и убедитесь, что в ней нет повторов;
         * удалите задачу, которая есть в истории, и проверьте, что при печати она не будет выводиться;
         * удалите эпик с тремя подзадачами и убедитесь, что из истории удалился как сам эпик, так и все его подзадачи.
         */
        System.out.println("\n------------Созадаём задачи--------------");
        memoryManager.addTask(new Task("Задача-1", "Описание задачи с id 1", Status.NEW));
        memoryManager.addTask(new Task("Задача-2", "Описание задачи с id 2", Status.NEW));
        memoryManager.addEpicTask(new Epic("Эпик-1", "Описание эпика с id 3"));
        memoryManager.addEpicTask(new Epic("Эпик-2", "Описание эпика с id 4"));
        memoryManager.addSubTask(new Subtask("Подзадача-1", "Описание подзадачи с id 5", Status.NEW, 3));
        memoryManager.addSubTask(new Subtask("Подзадача-2", "Описание подзадачи с id 6", Status.NEW, 3));
        memoryManager.addSubTask(new Subtask("Подзадача-3", "Описание подзадачи с id 7", Status.NEW, 3));

        memoryManager.getTaskById(1);
        memoryManager.getEpicTaskById(4);
        memoryManager.getEpicTaskById(3);
        memoryManager.getTaskById(2);
        memoryManager.getEpicTaskById(3);
        memoryManager.getTaskById(1);

        System.out.println(memoryManager.getHistory());
        System.out.println("\n------------Удаляем задачу и эпик с тремя подзадачами под индексом 1 и 3--------------");

        memoryManager.deleteTaskById(1);
        memoryManager.deleteEpicById(3);

        System.out.println(memoryManager.getHistory());

        System.out.println("\n------------бращаемся по несущестующему индексу, удаляем всё --------------");

        //обращаемся по несущестующему индексу
        memoryManager.getEpicTaskById(3);
        memoryManager.getTaskById(1);
        memoryManager.getTaskById(999);

        //удаляем всё
        memoryManager.deleteAllTasks();
        memoryManager.deleteAllEpicTasks();

        System.out.println(memoryManager.getHistory());
    }
}
