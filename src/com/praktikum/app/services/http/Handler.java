package com.praktikum.app.services.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.praktikum.app.models.Epic;
import com.praktikum.app.models.Subtask;
import com.praktikum.app.models.Task;
import com.praktikum.app.services.http.utils.LocalDateTimeAdapter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Handler implements HttpHandler {
    private static final int INDEX_PATH_TYPE_TASK = 2;
    private static final int LENGTH_WITHOUT_QUERY = 2;
    private static final int INDEX_WITHOUT_QUERY = 1;
    private static final int INDEX_FOR_PARSE_ID_FROM_SUBSTRING = 3;
    private final HttpTaskManager httpTaskManager;
    private final Gson gson;

    public Handler(HttpTaskManager httpTaskManager) {
        this.httpTaskManager = httpTaskManager;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gson = gsonBuilder.create();
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            String[] path = exchange.getRequestURI().getPath().split("/");
            if (path.length == LENGTH_WITHOUT_QUERY && path[INDEX_WITHOUT_QUERY].equals("tasks")) {
                handlePriority(exchange);
                return;
            }
            switch (path[INDEX_PATH_TYPE_TASK]) {
                case "task" -> handleTasks(exchange);
                case "subtask" -> handleSubtask(exchange);
                case "epic" -> handleEpic(exchange);
                case "history" -> handleHistory(exchange);
            }
        } catch (IOException exception) {
            System.out.println("Ошибка при обработке запроса");
        } finally {
            exchange.close();
        }
    }

    protected void writeInResponseBody(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
    }

    private void handlePriority(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String query = exchange.getRequestURI().toString();
        if (requestMethod.equals("GET") && query.equals("/tasks/")) {
            String response = gson.toJson(httpTaskManager.getPrioritizedTasks());
            writeInResponseBody(exchange, response);
        }
    }

    private void handleTasks(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String query = exchange.getRequestURI().getQuery();
        switch (requestMethod) {
            case "GET": {
                if (query != null) {
                    String idTask = query.substring(INDEX_FOR_PARSE_ID_FROM_SUBSTRING);
                    Task task = httpTaskManager.getTaskById(Integer.parseInt(idTask));
                    String response = gson.toJson(task);
                    writeInResponseBody(exchange, response);
                    break;
                } else {
                    String response = gson.toJson(httpTaskManager.getTasks());
                    writeInResponseBody(exchange, response);
                    break;
                }
            }
            case "POST": {
                try {
                    String body = new String(exchange.getRequestBody().readAllBytes());
                    Task task = gson.fromJson(body, Task.class);
                    if (httpTaskManager.getTasks().contains(task)) {
                        httpTaskManager.updateTask(task);
                        exchange.sendResponseHeaders(200, 0);
                    } else {
                        httpTaskManager.addTask(task);
                        exchange.sendResponseHeaders(200, 0);
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                break;
            }
            case "DELETE": {
                if (query != null) {
                    String idTask = query.substring(3);
                    httpTaskManager.deleteTaskById(Integer.parseInt(idTask));
                    exchange.sendResponseHeaders(200, 0);
                } else {
                    httpTaskManager.deleteAllTasks();
                    exchange.sendResponseHeaders(200, 0);
                }
                break;
            }
        }
    }

    private void handleEpic(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String query = exchange.getRequestURI().getQuery();

        switch (requestMethod) {
            case "GET": {
                if (query != null) {
                    String idEpic = query.substring(3);
                    Epic epic = httpTaskManager.getEpicTaskById(Integer.parseInt(idEpic));
                    String response = gson.toJson(epic);
                    writeInResponseBody(exchange, response);
                    return;
                } else {
                    String response = gson.toJson(httpTaskManager.getEpics());
                    writeInResponseBody(exchange, response);
                    return;
                }
            }
            case "POST": {
                try {
                    InputStream inputStream = exchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes());
                    Epic epic = gson.fromJson(body, Epic.class);
                    if (httpTaskManager.getEpics().contains(epic)) {
                        httpTaskManager.updateEpic(epic);
                        exchange.sendResponseHeaders(200, 0);
                    } else {
                        httpTaskManager.addEpicTask(epic);
                        exchange.sendResponseHeaders(200, 0);
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return;
            }
            case "DELETE": {
                if (query != null) {
                    String idEpic = query.substring(INDEX_FOR_PARSE_ID_FROM_SUBSTRING);
                    httpTaskManager.deleteTaskById(Integer.parseInt(idEpic));
                    exchange.sendResponseHeaders(200, 0);
                } else {
                    httpTaskManager.deleteAllEpicTasks();
                    exchange.sendResponseHeaders(200, 0);
                }
            }
        }
    }

    private void handleSubtask(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String query = exchange.getRequestURI().getQuery();
        switch (requestMethod) {
            case "GET": {
                if (query != null) {
                    if (exchange.getRequestURI().getPath().replaceFirst("/tasks/subtask/", "").equals("epic")) {
                        String idEpic = query.substring(INDEX_FOR_PARSE_ID_FROM_SUBSTRING);
                        String response = gson.toJson(httpTaskManager.getEpicTaskById(Integer.parseInt(idEpic)));
                        writeInResponseBody(exchange, response);
                        return;
                    } else {
                        String idSubTask = query.substring(3);
                        Subtask subtask = httpTaskManager.getSubTaskById(Integer.parseInt(idSubTask));
                        String response = gson.toJson(subtask);
                        writeInResponseBody(exchange, response);
                        return;
                    }
                } else {
                    String response = gson.toJson(httpTaskManager.getSubtasks());
                    writeInResponseBody(exchange, response);
                    return;
                }
            }
            case "POST": {
                try {
                    InputStream inputStream = exchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes());
                    Subtask subtask = gson.fromJson(body, Subtask.class);
                    if (httpTaskManager.getSubtasks().contains(subtask)) {
                        httpTaskManager.updateSubtask(subtask);
                        exchange.sendResponseHeaders(200, 0);
                    } else {
                        httpTaskManager.addSubTask(subtask);
                        exchange.sendResponseHeaders(200, 0);
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return;
            }
            case "DELETE": {
                if (query != null) {
                    String id = query.substring(3);
                    httpTaskManager.deleteSubtaskById(Integer.parseInt(id));
                    exchange.sendResponseHeaders(200, 0);
                } else {
                    httpTaskManager.deleteAllSubTasks();
                    exchange.sendResponseHeaders(200, 0);
                }
            }
        }
    }

    private void handleHistory(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equals("GET")) {
            List<Task> history = httpTaskManager.getHistory();
            String response = gson.toJson(history);
            writeInResponseBody(exchange, response);
        }
    }
}
