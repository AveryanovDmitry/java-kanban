package test;

import com.praktikum.app.models.Epic;
import com.praktikum.app.models.Subtask;
import com.praktikum.app.models.Task;
import com.praktikum.app.models.utils.Status;
import com.praktikum.app.services.inMemoryManager.TaskManager;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;

    /** Для расчёта статуса Epic.
     * Пустой список подзадач.*/
    @Test
    public void emptyEpicTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        manager.addEpicTask(epic);

        assertEquals(Status.NEW, manager.getEpicTaskById(epic.getId()).getStatus(),
                "Ошибка при получении статуса из пустого эпика");
    }

    /** Для расчёта статуса Epic.
     * Все подзадачи со статусом NEW.*/
    @Test
    public void statusNewTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        manager.addEpicTask(epic);

        manager.addSubTask(new Subtask("Подзадача-0", "Описание подзадачи 0", Status.NEW, epic.getId()));
        manager.addSubTask(new Subtask("Подзадача-1", "Описание подзадачи 1", Status.NEW, epic.getId()));
        manager.addSubTask(new Subtask("Подзадача-2", "Описание подзадачи 2", Status.NEW, epic.getId()));

        assertEquals(Status.NEW, epic.getStatus(), "У эпика статус не NEW");
    }

    /** Для расчёта статуса Epic.
     * Все подзадачи со статусом DONE.*/
    @Test
    public void statusDoneTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        manager.addEpicTask(epic);

        manager.addSubTask(new Subtask("Подзадача-0", "Описание подзадачи 0", Status.DONE, epic.getId()));
        manager.addSubTask(new Subtask("Подзадача-1", "Описание подзадачи 1", Status.DONE, epic.getId()));
        manager.addSubTask(new Subtask("Подзадача-2", "Описание подзадачи 2", Status.DONE, epic.getId()));

        assertEquals(Status.DONE, epic.getStatus(), "У эпика статус не DONE");
    }

    /** Для расчёта статуса Epic.
     * Подзадачи со статусами NEW и DONE.*/
    @Test
    public void statusNewDoneTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        manager.addEpicTask(epic);

        manager.addSubTask(new Subtask("Подзадача-0", "Описание подзадачи 0", Status.NEW, epic.getId()));
        manager.addSubTask(new Subtask("Подзадача-1", "Описание подзадачи 1", Status.DONE, epic.getId()));

        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "У эпика статус не IN_PROGRESS");
    }

    /** Для расчёта статуса Epic.
     * Подзадачи со статусом IN_PROGRESS.*/
    @Test
    public void statusInProgressTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        manager.addEpicTask(epic);

        manager.addSubTask(new Subtask("Подзадача-0", "Описание подзадачи 0", Status.IN_PROGRESS,
                epic.getId()));
        manager.addSubTask(new Subtask("Подзадача-1", "Описание подзадачи 1", Status.IN_PROGRESS,
                epic.getId()));
        manager.addSubTask(new Subtask("Подзадача-2", "Описание подзадачи 2", Status.IN_PROGRESS,
                epic.getId()));

        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "У эпика статус не IN_PROGRESS");
    }

    /** Для двух менеджеров задач InMemoryTasksManager и FileBackedTasksManager.
     * Проверка методов добавления задач*/
    @Test
    public void addTaskTest() {
        Task task = new Task("Задача", "Описание задачи", Status.NEW);
        manager.addTask(task);

        final Task savedTask = manager.getTaskById(task.getId());

        assertNotNull(savedTask, "Задача после добавления не сохранилась.");
        assertEquals(task, savedTask, "Задача после сохранения была изменена или получена другая задача.");

        manager.addTask(new Task("Задача", "Описание задачи", Status.NEW));
        manager.addTask(new Task("Задача", "Описание задачи", Status.NEW));

        final List<Task> tasks = manager.getTasks();

        assertNotNull(tasks, "Не удалось получить список задач");
        assertEquals(task, tasks.get(0), "Список задач содержит не те задачи");
        assertEquals(3, tasks.size(), "Неверное количество задач");
    }


    @Test
    public void addEpicTaskTest() {
        Epic epic =  new Epic("Эпик", "Описание эпика");
        manager.addEpicTask(epic);

        final Epic savedEpic = manager.getEpicTaskById(epic.getId());

        assertNotNull(savedEpic, "Эпик уже не тот.");
        assertEquals(epic, savedEpic, "Эпик после сохранения был изменен или получен другой эпик.");

        manager.addEpicTask(new Epic("Эпик", "Описание эпика"));
        manager.addEpicTask(new Epic("Эпик", "Описание эпика"));
        final List<Epic> epics = manager.getEpics();

        assertNotNull(epic, "Эпики не возвращаются.");
        assertEquals(epic, epics.get(1), "Получили не те эпики.");
        assertEquals(3, epics.size(), "Неверное количество эпиков.");
    }

    @Test
    public void addSubTaskTest() {
        Epic epic =  new Epic("Эпик", "Описание эпика");
        manager.addEpicTask(epic);
        Subtask subtask =  new Subtask("Подзадача", "Описание подзадачи", Status.NEW, epic.getId());
        manager.addSubTask(subtask);

        final Task savedSubtask = manager.getSubTaskById(subtask.getId());

        assertNotNull(savedSubtask, "Подзадача уже не та.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");

        manager.addSubTask(new Subtask("Подзадача", "Описание подзадачи", Status.NEW, epic.getId()));
        manager.addSubTask(new Subtask("Подзадача", "Описание подзадачи", Status.NEW, epic.getId()));

        final List<Subtask> subtasks = manager.getSubtasks();

        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(subtask, subtasks.get(0), "Подзадача не совпала.");
        assertEquals(3, subtasks.size(), "Неверное количество подзадач.");
    }

    /**
     * Проверки обновления задач
     */
    @Test
    public void updateTaskTest() {
        Task task = new Task("Задача", "Описание задачи", Status.NEW);
        manager.addTask(task);

        manager.updateTask(new Task("Обновленная задача",
                "Описание обновлённой задачи", Status.IN_PROGRESS, task.getId()));

        final Task savedTask = manager.getTaskById(task.getId());

        assertNotNull(savedTask, "Задача не возвращается.");
        assertNotEquals(task, savedTask, "Задача не обновилась.");
        assertEquals(Status.IN_PROGRESS, savedTask.getStatus(), "Задача не обновилась.");

        final List<Task> tasks = manager.getTasks();

        assertNotEquals(task, tasks.get(0), "Задача не обновилась.");
    }

    @Test
    public void updateEpicTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        manager.addEpicTask(epic);

        manager.updateEpic(new Epic("Обновленный эпик", "Описание обновлённого эпика", epic.getId()));

        final Epic savedEpic = manager.getEpicTaskById(epic.getId());

        assertNotNull(savedEpic, "Задача не возвращается.");
        assertNotEquals(epic, savedEpic, "Задача не обновилась.");
        assertEquals("Обновленный эпик", savedEpic.getName(), "Задача не обновилась.");

        final List<Epic> epics = manager.getEpics();

        assertNotEquals(epic, epics.get(0), "Задача не обновилась.");
    }

    @Test
    public void updateSubTaskTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        manager.addEpicTask(epic);
        Subtask subtask =  new Subtask("Подзадача", "Описание подзадачи", Status.NEW, epic.getId());
        manager.addSubTask(subtask);
        System.out.println(subtask.getId());
        manager.updateSubtask(new Subtask("Обновленная подзадача", "Описание обновлённй подзадачи", Status.IN_PROGRESS, epic.getId(), subtask.getId()));

        final Subtask savedSubtask = manager.getSubTaskById(subtask.getId());

        assertNotNull(savedSubtask, "Задача не найдена.");
        assertNotEquals(subtask, savedSubtask, "Задача не обновилась.");
        assertEquals(Status.IN_PROGRESS, savedSubtask.getStatus(), "Задача не обновилась.");

        final List<Subtask> subtasks = manager.getSubtasks();

        assertNotEquals(subtask, subtasks.get(0), "Задача не обновилась.");
    }

    /**
     * проверка удаления задач
     */
    @Test
    public void deleteTaskByIdTest() {
        Task task = new Task("Задача", "Описание задачи", Status.NEW);
        manager.addTask(task);

        manager.deleteTaskById(task.getId());
        assertTrue(manager.getTasks().isEmpty(),"Задача не удалена.");
    }

    @Test
    public void deleteEpicByIdTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        manager.addEpicTask(epic);

        manager.deleteEpicById(epic.getId());
        assertTrue(manager.getEpics().isEmpty(),"Эпик не удален.");
    }

    @Test
    public void deleteSubtaskByIdTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        manager.addEpicTask(epic);
        Subtask subtask = new Subtask("Подзадача", "Описание подзадачи", Status.NEW, epic.getId());
        manager.addSubTask(subtask);

        manager.deleteSubtaskById(subtask.getId());
        assertTrue(manager.getSubtasks().isEmpty(),"Подзадача не удалена.");
    }

    @Test
    public void deleteAllTasksTest() {
        manager.addTask(new Task("Задача0", "Описание задачи0", Status.NEW));
        manager.addTask(new Task("Задача1", "Описание задачи1", Status.NEW));
        manager.addTask(new Task("Задача2", "Описание задачи2", Status.NEW));
        manager.addTask(new Task("Задача3", "Описание задачи3", Status.NEW));
        manager.addTask(new Task("Задача4", "Описание задачи4", Status.NEW));

        manager.deleteAllTasks();
        assertTrue(manager.getTasks().isEmpty(), "Задачи не удалены.");
    }

    @Test
    public void deleteAllEpicsTest() {
        manager.addEpicTask(new Epic("Эпик", "Описание эпика"));
        manager.addEpicTask(new Epic("Эпик", "Описание эпика"));
        manager.addEpicTask(new Epic("Эпик", "Описание эпика"));
        manager.addEpicTask(new Epic("Эпик", "Описание эпика"));
        manager.addEpicTask(new Epic("Эпик", "Описание эпика"));

        manager.deleteAllEpicTasks();
        assertTrue(manager.getEpics().isEmpty(),"Эпики не удалились.");
    }

    @Test
    public void deleteAllSubtasksTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        manager.addEpicTask(epic);
        manager.addSubTask(new Subtask("Подзадача", "Описание подзадачи", Status.NEW, epic.getId()));
        manager.addSubTask(new Subtask("Подзадача", "Описание подзадачи", Status.NEW, epic.getId()));
        manager.addSubTask(new Subtask("Подзадача", "Описание подзадачи", Status.NEW, epic.getId()));
        manager.addSubTask(new Subtask("Подзадача", "Описание подзадачи", Status.NEW, epic.getId()));

        manager.deleteAllSubTasks();
        assertTrue(manager.getSubtasks().isEmpty(),"Подзадачи не удалены.");
    }
}