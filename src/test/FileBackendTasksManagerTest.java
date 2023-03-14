package test;

import com.praktikum.app.exceptions.ManagerSaveException;
import com.praktikum.app.models.Epic;
import com.praktikum.app.models.Subtask;
import com.praktikum.app.models.Task;
import com.praktikum.app.models.utils.Status;
import com.praktikum.app.services.inFileManager.FileBackendTasksManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackendTasksManagerTest extends TaskManagerTest {
    File file = new File("src/test/test.csv");

    @BeforeEach
    public void beforeEach() {
        manager = new FileBackendTasksManager(file);
    }

    /**
     * Проверка работы по сохранению и восстановлению состояния.
     */
    @Test
    public void loadFromFileTest() {
        Task task = new Task("Задача", "Описание задачи", Status.NEW);
        manager.addTask(task);
        Epic epic = new Epic("Эпик", "Описание эпика");
        manager.addEpicTask(epic);
        Subtask subtask = new Subtask("Подзадача", "Описание подзадачи", Status.NEW, epic.getId());
        manager.addSubTask(subtask);

        manager = FileBackendTasksManager.loadFromFile(file);

        assertNotNull(manager.getTasks(), "Задачи не возвращаются.");
        assertNotNull(manager.getEpics(), "Задачи не возвращаются.");

        assertEquals(1, manager.getTasks().size(), "Неверное количество задач.");
        assertEquals(1, manager.getEpics().size(), "Неверное количество задач.");

        assertEquals(task, manager.getTaskById(task.getId()), "Загрузка из файла не работает.");
        assertEquals(epic, manager.getEpicTaskById(epic.getId()), "Загрузка из файла не работает.");
        assertEquals(subtask, manager.getSubTaskById(subtask.getId()), "Загрузка из файла не работает.");
    }

    /**
     * Проверка обработки исключения. Неправильный адрес файла.
     */
    @Test
    public void loadFromWrongAddressFileTest() {
        assertThrows(ManagerSaveException.class,
                () -> FileBackendTasksManager.loadFromFile(new File("неправильный адрес")));
    }

    /**
     * Для FileBackedTasksManager — проверка работы по сохранению и восстановлению состояния.
     * Пустой список задач.
     */
    @Test
    public void loadDataFromEmptyFileTest() throws IOException {
        Files.writeString(file.toPath(), ""); //очищаем файл после работы предыдущих тестов
        manager = FileBackendTasksManager.loadFromFile(file);
        assertTrue(manager.getTasks().isEmpty(), "Загрузка из файла не работает.");
        assertTrue(manager.getEpics().isEmpty(), "Загрузка из файла не работает.");
        assertTrue(manager.getSubtasks().isEmpty(), "Загрузка из файла не работает.");
    }

    /**
     * Для FileBackedTasksManager — проверка работы по сохранению и восстановлению состояния.
     * Эпик без подзадач.
     */
    @Test
    public void loadDataFromFileEpicWithoutSubtasksTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        manager.addEpicTask(epic);
        FileBackendTasksManager.loadFromFile(file);

        assertNotNull(manager.getEpics(), "Задачи не возвращаются.");
        assertEquals(1, manager.getEpics().size(), "Неверное количество задач.");
        assertEquals(epic, manager.getEpicTaskById(epic.getId()), "Загрузка из файла не работает.");
        assertTrue(manager.getSubtasks().isEmpty(), "Загрузка из файла не работает.");
    }

    /**
     * Проверка работы по сохранению и восстановлению состояния.
     * Пустой список истории.
     */
    @Test
    public void loadFromFileEmptyHistoryTest() {
        Task task = new Task("Задача", "Описание задачи", Status.NEW);
        manager.addTask(task);
        Epic epic = new Epic("Эпик", "Описание эпика");
        manager.addEpicTask(epic);
        manager = FileBackendTasksManager.loadFromFile(file);
        assertTrue(manager.getHistory().isEmpty(), "Загрузка из файла не работает.");
    }
}
