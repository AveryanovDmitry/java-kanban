package com.praktikum.app.service;

import com.praktikum.app.models.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task);

    List<Task>getHistory();

}
