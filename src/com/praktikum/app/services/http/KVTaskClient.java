package com.praktikum.app.services.http;


import com.praktikum.app.services.http.exceptions.KVTaskClientException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;

public class KVTaskClient {
    private static final String MESSAGE_FOR_REGISTER = "Ошибка в методе register, класса KVTaskClien";
    private static final String MESSAGE_FOR_CONNECT = "Убедитесь что запущен KVServer к которому пытается обратиться клиент";
    private static final String MESSAGE_FOR_PUT = "Ошибка в методе put, класса KVTaskClien";
    private static final String MESSAGE_FOR_LOAD = "Ошибка в методе load класса KVTaskClient";
    private final String apiToken;
    private final String url;

    public KVTaskClient(String url) {
        this.url = url;
        this.apiToken = this.register();
    }

    private String register() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url + "register"))
                    .header("Content-Type", "application/json")
                    .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request, handler);
            if (response.statusCode() != 200) {
                throw new KVTaskClientException(MESSAGE_FOR_REGISTER, new ConnectException());
            }
            return response.body();
        } catch (ConnectException e) {
            throw new KVTaskClientException(MESSAGE_FOR_CONNECT, e);
        } catch (IOException | InterruptedException e) {
            throw new KVTaskClientException(MESSAGE_FOR_REGISTER, e);
        }
    }

    /**
     * Метод void put(String key, String json) должен сохранять состояние менеджера задач через запрос POST /save/<ключ>?API_TOKEN=
     */
    public void put(String key, String json) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().POST(BodyPublishers.ofString(json))
                    .uri(URI.create(url + "save/" + key + "?API_TOKEN=" + apiToken))
                    .header("Content-Type", "application/json").build();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() != 200) {
                throw new KVTaskClientException(MESSAGE_FOR_PUT, new ConnectException());
            }
        } catch (InterruptedException | IOException e) {
            throw new KVTaskClientException(MESSAGE_FOR_PUT, e);
        }
    }

    /**
     * Метод String load(String key) должен возвращать состояние менеджера задач через запрос GET /load/<ключ>?API_TOKEN=.
     */
    public String load(String key) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().GET()
                    .uri(URI.create(url + "load/" + key + "?API_TOKEN=" + apiToken))
                    .header("Content-Type", "application/json").build();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() != 200) {
                throw new KVTaskClientException(MESSAGE_FOR_LOAD, new ConnectException());
            }
            return response.body();
        } catch (InterruptedException | IOException e) {
            throw new KVTaskClientException(MESSAGE_FOR_LOAD, e);
        }
    }
}