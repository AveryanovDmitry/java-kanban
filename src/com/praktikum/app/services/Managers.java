package com.praktikum.app.services;

import com.praktikum.app.services.inMemoryManager.HistoryManager;
import com.praktikum.app.services.inMemoryManager.InHistoryTaskManager;
import com.praktikum.app.services.inMemoryManager.InMemoryTaskManager;
import com.praktikum.app.services.inMemoryManager.TaskManager;

public class Managers {

    private Managers(){}

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InHistoryTaskManager();
    }

}
