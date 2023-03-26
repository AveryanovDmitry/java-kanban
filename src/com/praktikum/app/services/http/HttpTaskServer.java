package com.praktikum.app.services.http;

import com.praktikum.app.services.Managers;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer server;
    private final HttpTaskManager httpTaskManager;
    public HttpTaskServer() throws IOException {
        this.httpTaskManager = Managers.getDefault("http://localhost:8078/");
        this.httpTaskManager.load();
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
        this.server.createContext("/tasks", new Handler(httpTaskManager));
    }

    public void start() {
        System.out.println("HTTP-сервер запущен на " + PORT + " порту");
        this.server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Остановили сервер на порту" + PORT);
    }

    public HttpTaskManager getHttpTaskManager() {
        return httpTaskManager;
    }
}
