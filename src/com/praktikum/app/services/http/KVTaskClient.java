package com.praktikum.app.services.http;


import com.praktikum.app.services.http.exceptions.KVTaskClientException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;

public class KVTaskClient {
    private final String apiToken;
    private String url;

    public KVTaskClient(String url) {
        this.url = url;
        this.apiToken = this.register(url);
    }

    public String register(String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url + "register"))
                    .header("Content-Type", "application/json")
                    .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> send = client.send(request,handler);
            return send.body();
        } catch (IOException | InterruptedException e) {
            throw new KVTaskClientException("Ошибка в методе register, класса KVTaskClient", e);
        }
    }

    /** Метод void put(String key, String json) должен сохранять состояние менеджера задач через запрос POST /save/<ключ>?API_TOKEN= */
    public void put(String key, String json) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().POST(BodyPublishers.ofString(json))
                    .uri(URI.create(this.url + "save/" + key + "?API_TOKEN=" + this.apiToken))
                    .header("Content-Type", "application/json").build();
            client.send(request, BodyHandlers.ofString(StandardCharsets.UTF_8));
        } catch (InterruptedException | IOException e) {
            throw new KVTaskClientException("Ошибка в методе put, класса  KVTaskClient", e);
        }
    }

    /** Метод String load(String key) должен возвращать состояние менеджера задач через запрос GET /load/<ключ>?API_TOKEN=. */
    public String load(String key) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().GET()
                    .uri(URI.create(this.url + "load/" + key + "?API_TOKEN=" + this.apiToken))
                    .header("Content-Type", "application/json").build();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString(StandardCharsets.UTF_8));
            return response.body();
        } catch (InterruptedException | IOException e) {
            throw new KVTaskClientException("Ошибка в методе load класса KVTaskClient", e);
        }
    }
}