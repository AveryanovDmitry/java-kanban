package test;

import com.praktikum.app.models.Epic;
import com.praktikum.app.models.Task;
import com.praktikum.app.models.utils.Status;
import com.praktikum.app.services.Managers;
import com.praktikum.app.services.inMemoryManager.HistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    HistoryManager manager;

    @BeforeEach
    public void beforeEach() {
        manager = Managers.getDefaultHistory();
    }

    @Test
    void addTest() {
        manager.add(new Task("Задача", "Описание задачи", Status.NEW));
        final List<Task> history = manager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void removeTest() {
        Task task = new Task("Задача", "Описание задачи", Status.NEW);
        manager.add(task);
        manager.remove(task.getId());
        assertTrue(manager.getHistory().isEmpty(), "История не пустая.");
    }


    /** Для HistoryManager — тесты для всех методов интерфейса.
     *  Пустая история задач.*/
    @Test
    public void addTasksHistoryEmptyTest() {
        assertTrue(manager.getHistory().isEmpty(), "История не пустая.");
    }

    /**Дублирование.*/
    @Test
    public void testAddTasksHistoryDouble() {
        Task task = new Task("Задача", "Описание задачи", Status.NEW);
        manager.add(task);
        Epic epic = new Epic("Эпик", "Описание эпика");
        manager.add(epic);
        Task task1 = new Task("Задача", "Описание задачи", Status.NEW);
        manager.add(task1);

        assertNotNull(manager.getHistory(), "История пустая.");
        System.out.println(manager.getHistory());
        System.out.println(List.of(task, epic, task1));
        System.out.println(List.of(manager.getHistory()).equals(manager.getHistory()));
        assertNotEquals(List.of(task, epic, task1), manager.getHistory(), "Задачи дублируются.");
    }
}
