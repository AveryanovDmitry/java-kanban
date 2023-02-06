package com.praktikum.app.services;

import com.praktikum.app.models.Task;

import java.util.List;
import java.util.Set;

public interface HistoryManager {

    void add(Task task);

    List<Task>getHistory();

    void remove(int id);
}
