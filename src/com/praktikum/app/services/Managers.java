package com.praktikum.app.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.praktikum.app.services.http.HttpTaskManager;
import com.praktikum.app.services.http.utils.LocalDateTimeAdapter;
import com.praktikum.app.services.inMemoryManager.HistoryManager;
import com.praktikum.app.services.inMemoryManager.InHistoryTaskManager;

import java.time.LocalDateTime;

public class Managers {

    private Managers(){}

    public static HttpTaskManager getDefault() {
        String defaultUri = "http://localhost:8078/";
        return new HttpTaskManager(defaultUri);
    }
    public static HttpTaskManager getDefault(String uri) {
        return new HttpTaskManager(uri);
    }

    public static HistoryManager getDefaultHistory() {
        return new InHistoryTaskManager();
    }

    public static Gson buildGsonWithLocalDateTimeAdapter() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        return gsonBuilder.create();
    }
}
