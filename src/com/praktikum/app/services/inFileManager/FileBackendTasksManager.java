package com.praktikum.app.services.inFileManager;

import com.praktikum.app.exceptions.ManagerSaveException;
import com.praktikum.app.models.Epic;
import com.praktikum.app.models.Subtask;
import com.praktikum.app.models.Task;
import com.praktikum.app.models.utils.Status;
import com.praktikum.app.models.utils.TypeTask;
import com.praktikum.app.services.inMemoryManager.InMemoryTaskManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackendTasksManager extends InMemoryTaskManager {

    public static void main(String[] args) {
        FileBackendTasksManager fileManeger = new FileBackendTasksManager(new File("src/com/praktikum/app/services/inFileManager/test.csv"));
        fileManeger.createTask(new Task("Задача-1", "Описание задачи с id 1", Status.NEW, TypeTask.TASK));
        fileManeger.createTask(new Task("Задача-2", "Описание задачи с id 2", Status.NEW, TypeTask.TASK));
        fileManeger.createEpicTask(new Epic("Эпик-1", "Описание эпика с id 3"));
        fileManeger.createEpicTask(new Epic("Эпик-2", "Описание эпика с id 4"));
        fileManeger.createSubTask(new Subtask("Подзадача-1", "Описание подзадачи с id 5", Status.NEW, 3));
        fileManeger.createSubTask(new Subtask("Подзадача-2", "Описание подзадачи с id 6", Status.NEW, 3));
        fileManeger.createSubTask(new Subtask("Подзадача-3", "Описание подзадачи с id 7", Status.NEW, 3));
        fileManeger.getTaskById(1);
        fileManeger.getTaskById(2);
        fileManeger.getEpicTaskById(3);

        FileBackendTasksManager fileManeger1 = FileBackendTasksManager.loadFromFile(new File("src/com/praktikum/app/services/inFileManager/test.csv"));
        System.out.println("---- данные из нового файл менеджера --- ");

        System.out.println(fileManeger1.getTasks());
        System.out.println(fileManeger1.getHistory());
    }

    private File file;

    public FileBackendTasksManager(File file) {
        this.file = file;
    }

    /**
     * восстанавливаем данные менеджера из файла при запуске программы
     */
    public static FileBackendTasksManager loadFromFile(File file) {
        FileBackendTasksManager fileManager = new FileBackendTasksManager(file);
        try {
            String fileContent = Files.readString(Path.of(String.valueOf(file)));
            if (fileContent.isEmpty())
                return fileManager;
            String[] content = fileContent.split("\n");

            for (int i = 1; i < content.length; i++) {
                if (content[i].isEmpty()) {
                    break; //встретили пустую строку в файле, задачи закончились
                } else {
                    Task task = fileManager.fromString(content[i]);
                    fileManager.createTask(task);
                }
            }

            if (!content[content.length - 1].isEmpty()) {//проверяем наличие истории, после пустой строки
                List<Integer> history = historyFromString(content[content.length - 1]);
                for (Integer id : history) {
                    if (fileManager.getEpicTaskById(id) != null) {
                        fileManager.getHistoryManager().add(fileManager.getEpicTaskById(id));
                    } else if (fileManager.getSubTaskById(id) != null) {
                        fileManager.getHistoryManager().add(fileManager.getSubTaskById(id));

                    } else if (fileManager.getTaskById(id) != null) {
                        fileManager.getHistoryManager().add(fileManager.getTaskById(id));
                    }
                }
            }
            return fileManager;
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    /**
     * из строки в историю
     */
    public static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        if (value.isEmpty()) {
            return history;
        }
        String[] line = value.split(",");
        for (String str : line) {
            history.add(Integer.parseInt(str));
        }
        return history;
    }

    /**
     * из строки в задачу
     */
    public Task fromString(String string) {
        String name;
        String description;
        Status status;
        TypeTask type;
        int id;

        String[] arrStrTask = string.split(",");

        id = Integer.parseInt(arrStrTask[0]);
        type = TypeTask.valueOf(arrStrTask[1]);
        name = arrStrTask[2];
        status = Status.valueOf(arrStrTask[3]);
        description = arrStrTask[4];

        switch (type) {
            case TASK:
                Task task = new Task(name, description, status, type);
                task.setId(id);
                return task;
            case EPIC:
                Epic epic = new Epic(name, description, id, status);
                epic.setId(id);
                return epic;
            case SUB_TASK:
                Subtask subTask = new Subtask(name, description, status, Integer.parseInt(arrStrTask[5]), id);
                subTask.setId(id);
                return subTask;
        }
        return null;
    }

    /**
     * сохраняем текущее состояние менеджера в указанный файл
     */
    public void save() {
        try (Writer writer = new FileWriter(file)) {
            writer.write("id,type,name,status,description,epic\n");
            List<Task> tasks = super.getTasks();
            List<Epic> epics = super.getEpics();
            List<Subtask> subTasks = super.getSubtasks();

            for (Task task : tasks) {
                writer.write(toStringFormat(task));
            }
            for (Epic epic : epics) {
                writer.write(toStringFormat(epic));
            }
            for (Subtask subtask : subTasks) {
                writer.write(toStringFormat(subtask));
            }
            writer.write("\n");
            writer.write(toString(getHistory()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String toStringFormat(Task task) {
        String numberEpicFromSubTask = "";
        if (task.getClass() == Subtask.class) {
            numberEpicFromSubTask = ((Subtask) task).getEpicId().toString();
        }
        return String.format(
                "%s,%s,%s,%s,%s,%s\n",
                task.getId(),
                task.getType(),
                task.getName(),
                task.getStatus(),
                task.getDescription(),
                numberEpicFromSubTask
        );
    }

    /**
     * для сохранения и восстановления менеджера истории из CSV.
     */
    public static String toString(List<Task> history) {
        StringBuilder line = new StringBuilder();
        if (!history.isEmpty()) {
            for (Task task : history) {
                line.append(task.getId()).append(",");
            }
        }
        if (line.length() > 0) {
            line.deleteCharAt(line.length() - 1);
        }

        return line.toString();
    }

    @Override
    public Task getTaskById(int taskId) {
        Task task = super.getTaskById(taskId);
        save();
        return task;
    }

    @Override
    public Epic getEpicTaskById(int epicId) {
        Epic task = super.getEpicTaskById(epicId);
        save();
        return task;
    }

    @Override
    public Subtask getSubTaskById(int subtaskId) {
        Subtask task = super.getSubTaskById(subtaskId);
        save();
        return task;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpicTask(Epic epic) {
        super.createEpicTask(epic);
        save();
    }

    @Override
    public void createSubTask(Subtask subTask) {
        super.createSubTask(subTask);
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void deleteAllEpicTasks() {
        super.deleteAllEpicTasks();
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subTask) {
        super.updateSubtask(subTask);
        save();
    }

    @Override
    public void deleteTaskById(int taskId) {
        super.deleteTaskById(taskId);
        save();
    }

    @Override
    public void deleteEpicById(int epicId) {
        super.deleteEpicById(epicId);
        save();
    }


    @Override
    public void deleteSubtaskById(int subtaskId) {
        super.deleteSubtaskById(subtaskId);
        save();
    }
}
