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
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {

        TaskManager memoryManager = Managers.getDefault();


        System.out.println("\n------------Созадаём задачи--------------");
        memoryManager.addTask(new Task("Задача-1", "Описание задачи с id 1", Status.NEW, LocalDateTime.now(), 40));
        memoryManager.addTask(new Task("Задача-2", "Описание задачи с id 2", Status.NEW, LocalDateTime.now().plusHours(5), 30));
        memoryManager.addEpicTask(new Epic("Эпик-1", "Описание эпика с id 3"));
        memoryManager.addSubTask(new Subtask("Подзадача-1", "Описание подзадачи с id 5", Status.NEW, 3, LocalDateTime.now().plusHours(2), 30));
        memoryManager.addSubTask(new Subtask("Подзадача-1", "Описание подзадачи с id 5", Status.NEW, 3, LocalDateTime.now().plusHours(6), 30));
        //memoryManager.addSubTask(new Subtask("Подзадача-2", "Описание подзадачи с id 6", Status.NEW, 3));
        //memoryManager.addSubTask(new Subtask("Подзадача-3", "Описание подзадачи с id 7", Status.NEW, 3));

        memoryManager.getPrioritizedTasks().stream().forEach(System.out::println);
        System.out.println("\n------------Смотрим эпик--------------");
        System.out.println(memoryManager.getEpics().get(0));

//        System.out.println("\n------------Созадаём задачи с пересечением временем--------------");
//        memoryManager.addTask(new Task("Задача-1", "Описание задачи с id 1", Status.NEW, LocalDateTime.now().plusHours(5).plusMinutes(10), 40));
//        memoryManager.addTask(new Task("Задача-2", "Описание задачи с id 2", Status.NEW, LocalDateTime.now().plusHours(5), 30));
//        memoryManager.addEpicTask(new Epic("Эпик-1", "Описание эпика с id 3"));
//        memoryManager.addSubTask(new Subtask("Подзадача-1", "Описание подзадачи с id 5", Status.NEW, 3, LocalDateTime.now().plusHours(2), 30));
//        memoryManager.addSubTask(new Subtask("Подзадача-2", "Описание подзадачи с id 6", Status.NEW, 3));
//        memoryManager.addSubTask(new Subtask("Подзадача-3", "Описание подзадачи с id 7", Status.NEW, 3));

//        memoryManager.getPrioritizedTasks().stream().forEach(System.out::println);
//        System.out.println(memoryManager.getEpics().get(0));

    }
}
