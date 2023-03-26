package com.praktikum.app;

import com.praktikum.app.models.Epic;
import com.praktikum.app.models.Subtask;
import com.praktikum.app.models.Task;
import com.praktikum.app.models.utils.Status;
import com.praktikum.app.services.http.HttpTaskManager;
import com.praktikum.app.services.http.HttpTaskServer;
import com.praktikum.app.services.http.KVServer;

import java.io.IOException;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        HttpTaskServer httpServer = new HttpTaskServer();
        httpServer.start();

        HttpTaskManager httpTaskManager = httpServer.getHttpTaskManager();
        httpTaskManager.addTask(new Task("Задача", "Описание задач", Status.NEW));
        httpTaskManager.addTask(new Task("Задач", "Описание задачи ", Status.NEW));
        httpTaskManager.addTask(new Task("Задача", "Описание задач", Status.NEW));
        httpTaskManager.addTask(new Task("Задача-4", "Описание задачи с id 4", Status.NEW, LocalDateTime.now(), 40));
        httpTaskManager.addTask(new Task("Задача-5", "Описание задачи с id 5", Status.NEW, LocalDateTime.now().plusHours(5), 30));
        httpTaskManager.addEpicTask(new Epic("Эпик-6", "Описание эпика с id 6"));
        httpTaskManager.addSubTask(new Subtask("Подзадача-1", "Описание подзадачи с id 7", Status.NEW, 6, LocalDateTime.now().plusHours(2), 30));
        httpTaskManager.addSubTask(new Subtask("Подзадача-1", "Описание подзадачи с id 8", Status.NEW, 6, LocalDateTime.now().plusHours(6), 30));
    }
}
