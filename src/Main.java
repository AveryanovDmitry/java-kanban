import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import Tasks.Utils.Status;

import java.util.Collection;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        SubTask subTask1 = manager.createSubTask("Подзадача-1", "Описание подзадачи-1");
        SubTask subTask2 = manager.createSubTask("Подзадача-2", "Описание подзадачи-3");
        SubTask subTask3 = manager.createSubTask("Подзадача-3", "Описание подзадачи-2");

        Epic epic1 = manager.createEpic("Эпичная задача-1", "Описание эпика-1", subTask1);
        Epic epic2 = manager.createEpic("Эпичная задача-2", "Описание эпика-2", subTask2, subTask3);

        System.out.println("--------------------До изменений----------------------");
        System.out.println(epic1.getName() + " :");
        epic1.getSubTaskList().forEach(System.out::println);

        System.out.println(epic2.getName() + " :");
        epic2.getSubTaskList().forEach(System.out::println);

        System.out.println("\n-----------------Изменены статусы задач---------------");
        manager.updateTask(epic1, Status.IN_PROGRESS);
        manager.updateTask(epic2, Status.IN_PROGRESS);

        manager.getAllTasks().forEach(System.out::println);

    }
}
