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
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileBackendTasksManager extends InMemoryTaskManager {
    private static final String TITLE_FOR_FILE = "id,type,name,status,description,epic,time_start,duration\n";
    private final File file;

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

            int emptyLineFlag = 0;
            for (int i = 1; i < content.length; i++) {
                if (content[i].isEmpty()) {
                    emptyLineFlag = 1;
                    break; //встретили пустую строку в файле, задачи закончились
                } else {
                    Task task = fileManager.fromString(content[i]);
                    if (fileManager.id <= task.getId()) {
                        fileManager.id = task.getId() + 1; //получаем следующее за текущим максимальным id значение
                    }
                    switch (task.getType()) {
                        case TASK:
                            fileManager.tasks.put(task.getId(), task);
                            fileManager.addPrioritizedTask(task);
                            break;
                        case EPIC:
                            fileManager.epics.put(task.getId(), (Epic) task);
                            break;
                        case SUB_TASK:
                            int subId = task.getId();
                            fileManager.subtasks.put(subId, (Subtask) task);
                            Epic epic = fileManager.epics.get(fileManager.subtasks.get(subId).getEpicId());
                            if (epic != null) {
                                epic.addToSubTasks(subId);
                                fileManager.addPrioritizedTask(task);
                                if (task.getStartTime() != null) {
                                    fileManager.updateTimeEpic(epic);
                                }
                            }
                            break;
                    }
                }
            }

            if (!content[content.length - 1].isEmpty() && emptyLineFlag == 1) {//проверяем наличие истории, после пустой строки
                List<Integer> history = historyFromString(content[content.length - 1]);
                for (Integer id : history) {
                    Task task;
                    if ((task = fileManager.epics.get(id)) != null) {
                        fileManager.historyManager.add(task);
                    } else if ((task = fileManager.subtasks.get(id)) != null) {
                        fileManager.historyManager.add(task);
                    } else if ((task = fileManager.tasks.get(id)) != null) {
                        fileManager.historyManager.add(task);
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
    private static List<Integer> historyFromString(String value) {
        if (value.isEmpty()) {
            return Collections.emptyList();
        }
        List<Integer> history = new ArrayList<>();
        String[] line = value.split(",");
        for (String str : line) {
            history.add(Integer.parseInt(str));
        }
        return history;
    }

    private LocalDateTime stringToLocalDate(String str) {
        String[] date = str.split("\\.");
        int day = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        String[] dateTime = date[2].split(" ");
        int year = Integer.parseInt(dateTime[0]);
        String[] time = dateTime[1].split(":");
        int hours = Integer.parseInt(time[0]);
        int minutes = Integer.parseInt(time[1]);

        return LocalDateTime.of(year, month, day, hours, minutes);
    }


    /**
     * из строки в задачу
     */
    private Task fromString(String string) {
        String name;
        String description;
        Status status;
        TypeTask type;
        int id;
        LocalDateTime startTime = null;
        int duration = 0;

        String[] arrStrTask = string.split(",");

        id = Integer.parseInt(arrStrTask[0]);
        type = TypeTask.valueOf(arrStrTask[1]);
        name = arrStrTask[2];
        status = Status.valueOf(arrStrTask[3]);
        description = arrStrTask[4];
        if (!arrStrTask[6].equals("Время не указано")) {
            startTime = stringToLocalDate(arrStrTask[6]);
        }
        if (!arrStrTask[7].equals("Продолжительность не указана")) {
             String[] tmp = arrStrTask[7].split(" ");
             duration = Integer.parseInt(tmp[0]);
        }

        switch (type) {
            case TASK:
                Task task = new Task(name, description, status, startTime, duration);
                task.setId(id);
                return task;
            case EPIC:
                Epic epic = new Epic(name, description, id, status, startTime, duration);
                epic.setId(id);
                return epic;
            case SUB_TASK:
                Subtask subTask = new Subtask(name, description, status, Integer.parseInt(arrStrTask[5]), id, startTime, duration);
                subTask.setId(id);
                return subTask;
        }
        return null;
    }

    /**
     * сохраняем текущее состояние менеджера в указанный файл
     */
    private void save() {
        try (Writer writer = new FileWriter(file)) {
            writer.write(TITLE_FOR_FILE);
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

    private String toStringFormat(Task task) {
        String numberEpicFromSubTask = "";
        if (task.getType() == TypeTask.SUB_TASK) {
            numberEpicFromSubTask = ((Subtask) task).getEpicId().toString();
        }
        return String.format(
                "%s,%s,%s,%s,%s,%s,%s,%s\n",
                task.getId(),
                task.getType(),
                task.getName(),
                task.getStatus(),
                task.getDescription(),
                numberEpicFromSubTask,
                task.timeToString(task.getStartTime()),
                task.durationToString()
        );
    }

    /**
     * для сохранения и восстановления менеджера истории из CSV.
     */
    private static String toString(List<Task> history) {
        StringBuilder line = new StringBuilder();

        for (Task task : history) {
            line.append(task.getId()).append(",");
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
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpicTask(Epic epic) {
        super.addEpicTask(epic);
        save();
    }

    @Override
    public void addSubTask(Subtask subTask) {
        super.addSubTask(subTask);
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
