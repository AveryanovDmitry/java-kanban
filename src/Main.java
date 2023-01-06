import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import Tasks.Utils.Status;

public class Main {
    public static void main(String[] args) {
//        ManagerTask managerTask = new ManagerTask();
//        ManagerEpic managerEpic = new ManagerEpic();
//        ManagerSubtask managerSubtask =
        Manager manager = new Manager();
//
//        SubTask subTask1 = new SubTask("Подзадача-1", "Описание подзадачи-1");
//        SubTask subTask2 = new SubTask("Подзадача-2", "Описание подзадачи-3");
//        SubTask subTask3 = new SubTask("Подзадача-3", "Описание подзадачи-2");
//
//        Epic epic1 = manager.createEpic("Эпичная задача-1", "Описание эпика-1", subTask1);
//        Epic epic2 = manager.createEpic("Эпичная задача-2", "Описание эпика-2", subTask2, subTask3);
//
//        System.out.println("--------------------До изменений----------------------");
//        System.out.println(epic1.getName() + " :");
//        epic1.getSubTaskList().forEach(System.out::println);
//
//        System.out.println(epic2.getName() + " :");
//        epic2.getSubTaskList().forEach(System.out::println);
//
//        System.out.println("\n-----------------Изменены статусы задач---------------");
//        manager.updateTask(epic1, Status.IN_PROGRESS);
//        manager.updateTask(epic2, Status.IN_PROGRESS);
//
//        manager.getAllTasks().forEach(System.out::println);

        System.out.println("\n------------Созадаём задачи--------------");
        manager.createTask(new Task("Задача-1", "Описание задачи-1", Status.NEW));
        manager.createTask(new Task("Задача-2", "Описание задачи-2", Status.NEW));
        manager.createEpicTask(new Epic("Эпик-1", "Описание эпика-1", Status.NEW));
        manager.createEpicTask(new Epic("Эпик-2", "Описание эпика-2", Status.NEW));
        manager.createSubTask(new SubTask("Подзадача-1", "Описание подзадачи-1", Status.NEW, 1));
        manager.createSubTask(new SubTask("Подзадача-2", "Описание подзадачи-2", Status.NEW, 1));
        manager.createSubTask(new SubTask("Подзадача-3", "Описание подзадачи-3", Status.NEW, 2));

        System.out.println(manager.getTasksMap());
        System.out.println(manager.getEpicTasksMap());
        System.out.println(manager.getSubTasksMap());

        manager.updateTask(new Task("Задача-1", "Обновлённое описание задачи-1", Status.IN_PROGRESS));
        manager.updateEpic(new Epic("Эпик-1", "Описание обновлённого эпика", Status.IN_PROGRESS));
        manager.updateSubTask(new SubTask("Подзадача-1", "Описание обновлённой подзадачи-1", Status.IN_PROGRESS, 1));
        manager.updateSubTask(new SubTask("Подзадача-2", "Описание обновлённой подзадачи-2", Status.IN_PROGRESS, 1));

        System.out.println("\n---------Задачи после обновления---------");
        System.out.println(manager.getTasksMap());
        System.out.println(manager.getEpicTasksMap());
        System.out.println(manager.getSubTasksMap());


        manager.deleteTaskById(1);
        manager.deleteEpicById(1);
        System.out.println("\n---------Задачи после удаления---------");
        System.out.println(manager.getTasksMap());
        System.out.println(manager.getEpicTasksMap());
        System.out.println(manager.getSubTasksMap());
    }
}
