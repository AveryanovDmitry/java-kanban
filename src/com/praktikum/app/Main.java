package com.praktikum.app;

import com.praktikum.app.models.Epic;
import com.praktikum.app.models.Subtask;
import com.praktikum.app.models.Task;
import com.praktikum.app.models.utils.Status;
import com.praktikum.app.services.Managers;
import com.praktikum.app.services.inMemoryManager.TaskManager;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {

        TaskManager memoryManager = Managers.getDefault();

        System.out.println("\n------------Созадаём задачи--------------");
        memoryManager.addTask(new Task("Задача", "Описание задач", Status.NEW));
        memoryManager.addTask(new Task("Задач", "Описание задачи ", Status.NEW));
        memoryManager.addTask(new Task("Задача", "Описание задач", Status.NEW));
        memoryManager.addTask(new Task("Задача-4", "Описание задачи с id 4", Status.NEW, LocalDateTime.now(), 40));
        memoryManager.addTask(new Task("Задача-5", "Описание задачи с id 5", Status.NEW, LocalDateTime.now().plusHours(5), 30));
        memoryManager.addEpicTask(new Epic("Эпик-6", "Описание эпика с id 6"));
        memoryManager.addSubTask(new Subtask("Подзадача-1", "Описание подзадачи с id 7", Status.NEW, 6, LocalDateTime.now().plusHours(2), 30));
        memoryManager.addSubTask(new Subtask("Подзадача-1", "Описание подзадачи с id 8", Status.NEW, 6, LocalDateTime.now().plusHours(6), 30));
        memoryManager.addTask(new Task("Задача", "Описание задач", Status.NEW));
        memoryManager.addTask(new Task("Задач", "Описание задачи ", Status.NEW));
        memoryManager.addTask(new Task("Задач", "Описание задачи ", Status.NEW));
        memoryManager.addTask(new Task("Задача", "Описание задач", Status.NEW));
        memoryManager.addTask(new Task("Задач", "Описание задачи ", Status.NEW));
        memoryManager.addSubTask(new Subtask("Подзадача-2", "Описание подзадачи с id 6", Status.NEW, 3));
       memoryManager.addSubTask(new Subtask("Подзадача-3", "Описание подзадачи с id 7", Status.NEW, 3));
        memoryManager.addTask(new Task("Задача-5", "Описание задачи с id 5", Status.NEW, LocalDateTime.now().plusDays(3), 30));

        System.out.println("-----------------------");
        memoryManager.getPrioritizedTasks().stream().forEach(System.out::println);
        System.out.println("\n------------Смотрим эпик--------------");
        System.out.println(memoryManager.getEpics().get(0));
    }
}
