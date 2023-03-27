package test;

import com.praktikum.app.models.Epic;
import com.praktikum.app.models.Subtask;
import com.praktikum.app.models.Task;
import com.praktikum.app.models.utils.Status;
import com.praktikum.app.services.Managers;
import com.praktikum.app.services.http.HttpTaskManager;
import com.praktikum.app.services.http.KVServer;
import com.praktikum.app.services.inMemoryManager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager>{
    KVServer kvServerTest;
    @BeforeEach
    public void beforeEach() throws IOException {
        kvServerTest = new KVServer();
        kvServerTest.start();
        manager = new HttpTaskManager("http://localhost:8078/", false);
    }
    @AfterEach
    void stopHttpTaskManagerTest() {
        kvServerTest.stop();
    }

    @Test
    void testCheckTaskAfterLoad(){
        manager.addEpicTask(new Epic("Эпик-1", "Описание эпика с id 1"));
        manager.addSubTask(new Subtask("Подзадача-1", "Описание подзадачи с id 2", Status.NEW,1,
                LocalDateTime.of(2025, 3, 26, 11, 11), 11));
        manager.addTask(new Task("Задача-1", "Описание задачи с id 3", Status.NEW,
                LocalDateTime.of(2024, 3, 1, 1, 1), 11));
        manager.addTask(new Task("Задача-2", "Описание задачи с id 4", Status.NEW));
        manager.addTask(new Task("Задачa-3", "Описание задачи с id 5", Status.NEW));
        manager.addTask(new Task("Задача-4", "Описание задачи c id 6", Status.NEW));
        manager.addTask(new Task("Задача-5", "Описание задачи с id 7", Status.NEW, LocalDateTime.now(), 40));
        manager.addTask(new Task("Задача-6", "Описание задачи с id 8", Status.NEW, LocalDateTime.now().plusDays(20), 30));
        manager.addEpicTask(new Epic("Эпик-2", "Описание эпика с id 9"));
        manager.addSubTask(new Subtask("Подзадача-2", "Описание подзадачи с id 10", Status.NEW, 9, LocalDateTime.now().plusHours(2), 30));
        manager.addSubTask(new Subtask("Подзадача-3", "Описание подзадачи с id 11", Status.NEW, 9, LocalDateTime.now().plusHours(3), 30));
        manager.addEpicTask(new Epic("Эпик-3", "Описание эпика с id 12"));
        manager.addSubTask(new Subtask("Подзадача-4", "Описание подзадачи с id 13", Status.NEW, 12, LocalDateTime.now().plusHours(4), 30));
        manager.addSubTask(new Subtask("Подзадача-5", "Описание подзадачи с id 14", Status.NEW, 12, LocalDateTime.now().plusHours(5), 30));
        manager.addEpicTask(new Epic("Эпик-4", "Описание эпика с id 15"));
        manager.addSubTask(new Subtask("Подзадача-6", "Описание подзадачи с id 16", Status.NEW, 15, LocalDateTime.now().plusHours(6), 30));
        manager.addSubTask(new Subtask("Подзадача-7", "Описание подзадачи с id 17", Status.NEW, 15, LocalDateTime.now().plusHours(7), 30));

        TaskManager testHttpTaskManager = Managers.getDefault("http://localhost:8078/", true);

        assertEquals(testHttpTaskManager.getTasks(), manager.getTasks(), "Список задач после выгрузки не совпададает");
        assertEquals(testHttpTaskManager.getTasks().size(), manager.getTasks().size(), "размеры списков задач не совпали");

        assertEquals(testHttpTaskManager.getEpics(), manager.getEpics(), "Список задач после выгрузки не совпададает");
        assertEquals(testHttpTaskManager.getEpics().size(), manager.getEpics().size(), "размеры списков задач не совпали");

        assertEquals(testHttpTaskManager.getSubtasks(), manager.getSubtasks(), "Список задач после выгрузки не совпададает");
        assertEquals(testHttpTaskManager.getSubtasks().size(), manager.getSubtasks().size(), "размеры списков задач не совпали");

        assertEquals(testHttpTaskManager.getPrioritizedTasks(), manager.getPrioritizedTasks(), "Список задач после выгрузки не совпададает");
        assertEquals(testHttpTaskManager.getPrioritizedTasks().size(), manager.getPrioritizedTasks().size(), "размеры списков задач не совпали");

        assertEquals(testHttpTaskManager.getHistory(), manager.getHistory(), "Список задач после выгрузки не совпададает");
        assertEquals(testHttpTaskManager.getHistory().size(), manager.getHistory().size(), "размеры списков задач не совпали");
    }
}