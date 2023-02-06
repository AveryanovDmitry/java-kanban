package com.praktikum.app.services;

public class Managers {

    private Managers(){}

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InHistoryTaskManager();
    }

}
