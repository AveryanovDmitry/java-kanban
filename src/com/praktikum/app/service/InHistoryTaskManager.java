package com.praktikum.app.service;

import com.praktikum.app.models.Task;

import java.util.ArrayList;
import java.util.List;

public class InHistoryTaskManager implements HistoryManager{

    private final static int HISTORY_SIZE = 10;
    private final List<Task> history = new ArrayList<>();

    public void add(Task task) {

        if (history.size() == HISTORY_SIZE) {
            history.remove(0);
        }
        if (task != null) {
            history.add(task);
        }
    }

    public List<Task> getHistory() {
        return history;
    }
}
