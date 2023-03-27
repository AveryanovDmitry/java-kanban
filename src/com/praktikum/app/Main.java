package com.praktikum.app;

import com.praktikum.app.models.Epic;
import com.praktikum.app.models.Subtask;
import com.praktikum.app.models.Task;
import com.praktikum.app.models.utils.Status;
import com.praktikum.app.services.Managers;
import com.praktikum.app.services.http.HttpTaskManager;
import com.praktikum.app.services.http.HttpTaskServer;
import com.praktikum.app.services.http.KVServer;
import com.praktikum.app.services.inMemoryManager.InMemoryTaskManager;
import com.praktikum.app.services.inMemoryManager.TaskManager;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        HttpTaskServer server = new HttpTaskServer();

        server.start();

        TaskManager taskManager = server.getTaskManager();
        taskManager.addTask(new Task("Задача-1", "Описание задачи с id 1", Status.NEW,
                LocalDateTime.of(2024, 3, 1, 1, 1), 11));

        System.out.println(taskManager.getTasks());
    }
}
