package main.java.test;

import main.java.exceptions.EpicNotFoundException;
import main.java.exceptions.TaskNotFoundException;
import main.java.manager.InMemoryTaskManager;
import main.java.manager.TaskManager;
import main.java.tasks.Epic;
import main.java.tasks.Task;
import main.java.tasks.Status;
import main.java.tasks.SubTask;

public class TaskManagerTest {
    public static void main(String[] args) {
        runTest();
    }

    public static void runTest() {
        TaskManager manager = new InMemoryTaskManager();

        //Создаем 2 отдельные задачи
        manager.createNewSingleTask("SingleTask №1", "First single task");
        manager.createNewSingleTask("SingleTask №2", "Second single task");

        //Создаем 2 эпика
        manager.createNewEpic("Epic №1", "Epic number one");
        manager.createNewEpic("Epic №2", "Epic number two");

        //Создаем 4 подзадачи, по 2 подзадачи у каждого эпика
        manager.createNewSubTask("SubTask №1", "SubTask №1 by Epic №1", 2);
        manager.createNewSubTask("SubTask №2", "SubTask №2 by Epic №1", 2);
        manager.createNewSubTask("SubTask №1", "SubTask №1 by Epic №2", 3);
        manager.createNewSubTask("SubTask №2", "SubTask №2 by Epic №2", 3);

        //Выводим все задачи вместе и по группам
        System.out.println("Список всех задач:");
        System.out.println(manager.getAllTasks());
        System.out.println("\nСписок всех одиночных задач:");
        System.out.println(manager.getAllSingleTasks());
        System.out.println("\nСписок всех эпиков:");
        System.out.println(manager.getAllEpic());
        System.out.println("\nСписок всех подзадач:");
        System.out.println(manager.getAllSubTasks());

        //Вносим изменения в отдельные задачи, меняем описание и статус. Выводим данную задачу.
        System.out.println("\nВнесены изменения в SingleTask №1 и SingleTask №2");

        System.out.println("\tSingleTask №1 до изменений: " + manager.getTaskById(0));

        Task firstTask = new Task(0, "SingleTask №1", "Updated first single task", Status.DONE);
        manager.updateTask(firstTask);

        System.out.println("\tSingleTask №1 после изменений: " + manager.getTaskById(0));

        System.out.println("\tSingleTask №2 до изменений: " + manager.getTaskById(1));

        Task secondTask = new Task(1, "SingleTask №2", "Updated first single task", Status.IN_PROGRESS);
        manager.updateTask(secondTask);

        System.out.println("\tSingleTask №2 после изменений: " + manager.getTaskById(1));

        //Меняем статус у подзадач первого эпика. Выводим все эпики, проверяем статусы
        System.out.println("\nИзменен статус у подзадач Epic №1");
        System.out.println("\tEpic №1 до изменений: " + manager.getTaskById(2) + "\n" +
                "    Задачи Epic №1 до изменений: " + manager.getAllSubTasksByEpicId(2));

        SubTask firstSubTask = new SubTask(4, "SubTask №1", "SubTask №1 by Epic №1", Status.DONE, 2);
        SubTask secondSubTask = new SubTask(5, "SubTask №2", "SubTask №2 by Epic №1", Status.DONE, 2);
        manager.updateTask(firstSubTask);
        manager.updateTask(secondSubTask);

        System.out.println("\tEpic №1 после изменений: " + manager.getTaskById(2) + "\n" +
                "\tЗадачи Epic №1 после изменений: " + manager.getAllSubTasksByEpicId(2));

        //Проверяем, что при попытке создать сабтаск с epicId, которого нет и при попытке получить данные по
        //несуществующему эпику выбрасывается исключение
        try {
            System.out.println("\nПолучаем подзадачи по id = 1 (проверяем, что выбрасывается исключение)");
            System.out.println(manager.getAllSubTasksByEpicId(10));
        } catch (EpicNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println("\nСоздаем подзадачу с epicId = 4 (проверяем, что выбрасывается исключение)");
            manager.createNewSubTask("SubTask", "SubTask", 1);
        } catch (EpicNotFoundException e) {
            System.out.println(e.getMessage());
        }

        //Меняем epicId у первой подзадачи первого эпика
        System.out.println("\nИзменили epicId у первой подзадачи первого эпика. " +
                "Проверяем, что у первого эпика данная подзадача пропала, а у второго наборот появилась");

        System.out.println("Список подзадач первого эпика до изменений: " + manager.getAllSubTasksByEpicId(2));
        System.out.println("Список подзадач второго эпика до изменений: " + manager.getAllSubTasksByEpicId(3));

        firstSubTask = new SubTask(4, "SubTask №1", "SubTask №1 by Epic №1", Status.DONE, 3);
        manager.updateTask(firstSubTask);

        System.out.println("Список подзадач первого эпика после изменений: " + manager.getAllSubTasksByEpicId(2));
        System.out.println("Список подзадач второго эпика после изменений: " + manager.getAllSubTasksByEpicId(3));

        //Удаляем одну подзадачу второго эпика и выводим оставшийся список подзадач
        System.out.println("\nУдаляем одну подзадачу второго эпика и выводим список оставшихся подзадач");

        System.out.println("Список подзадач до удаления: " + manager.getAllSubTasksByEpicId(3));

        manager.removeTaskById(4);

        System.out.println("Список подзадач после удаления: " + manager.getAllSubTasksByEpicId(3));

        //Меняем статусы подзадач второго эпика, проверяем, что статус эпика изменился
        System.out.println("\nМеняем статусы подзадач второго эпика, проверяем, что статус эпика изменился");

        System.out.println("Статус эпика до изменений: " + manager.getTaskById(3).getStatus());
        System.out.println("Статусы задач до изменений:");

        for (SubTask subTask : manager.getAllSubTasksByEpicId(3)) {
            System.out.println("\tНазвание задачи: " + subTask.getName() + " Статус: " + subTask.getStatus());
        }

        firstSubTask = new SubTask(6, "SubTask №1", "SubTask №1 by Epic №2", Status.IN_PROGRESS, 3);
        secondSubTask = new SubTask(7, "SubTask №2", "SubTask №2 by Epic №2", Status.IN_PROGRESS, 3);

        manager.updateTask(firstSubTask);
        manager.updateTask(secondSubTask);

        System.out.println("Статус эпика после изменений: " + manager.getTaskById(3).getStatus());
        System.out.println("Статусы задач после изменений:");

        for (SubTask subTask : manager.getAllSubTasksByEpicId(3)) {
            System.out.println("\tНазвание задачи: " + subTask.getName() + " Статус: " + subTask.getStatus());
        }

        //Удаляем последнюю задачу первого эпика, проверяем, что статус эпика изменился
        System.out.println("\nУдаляем последнюю подзадачу первого эпика, проверяем, что статус эпика изменился");

        System.out.println("Статус эпика до изменений: " + manager.getTaskById(2).getStatus());
        System.out.println("Список подзадач первого эпика до удаления: " + manager.getAllSubTasksByEpicId(2));

        manager.removeTaskById(5);

        System.out.println("Статус эпика после изменений: " + manager.getTaskById(2).getStatus());

        System.out.println("Список подзадач первого эпика после удаления: " + manager.getAllSubTasksByEpicId(2));

        //Удаляем второй эпик, проверяем, что его сабтаски стали отдельными задачами
        System.out.println("\nУдаляем второй эпик, проверяем, что его сабтаски стали отдельными задачами");
        manager.removeTaskById(3);
        try {
            System.out.println(manager.getTaskById(3));
        } catch (TaskNotFoundException t) {
            System.out.println(t.getMessage());
        }
        System.out.println(manager.getAllSingleTasks());

        //Проверяем корректность работы переопределенного equals
        System.out.println("\nПроверяем корректность работы переопределенного equals");

        manager.createNewSingleTask("TestEquals", "testing method equals");
        Task task = new Task(8, "TestEquals", "testing method equals", Status.NEW);

        System.out.println("Создана первая задача: " + manager.getTaskById(8));
        System.out.println("Создана вторая задача: " + task);
        System.out.println("Задачи равны: " + manager.getTaskById(8).equals(task));
        System.out.println("hashCode первой задачи: " + manager.getTaskById(8).hashCode());
        System.out.println("hashCode второй задачи: " + task.hashCode());

        manager.createNewEpic("TestEquals", "testing method equals");
        manager.createNewSubTask("TestEquals", "testing method equals", 9);
        Epic epic = new Epic(9, "TestEquals", "testing method equals");
        SubTask subTask = new SubTask(10, "TestEquals", "testing method equals", Status.NEW, 9);
        epic.addSubTask(10, subTask);

        System.out.println("Создан первый эпик: " + manager.getTaskById(9));
        System.out.println("Создан второй эпик: " + epic);
        System.out.println("Создана первая подзадача: " + manager.getTaskById(10));
        System.out.println("Создана вторая подзадача: " + subTask);
        System.out.println("Подзадачи равны: " + manager.getTaskById(10).equals(subTask));
        System.out.println("Эпики равны: " + manager.getTaskById(9).equals(epic));
        System.out.println("hashCode первого эпика: " + manager.getTaskById(9).hashCode());
        System.out.println("hashCode второго эпика: " + epic.hashCode());
        System.out.println("hashCode первой подзадачи: " + manager.getTaskById(10).hashCode());
        System.out.println("hashCode второй подзадачи: " + subTask.hashCode());

        //Проверяем работу отражения истории просмотра задач
        System.out.println("\nПроверяем работу отражения истории просмотра задач");

        for (int i = 0; i < 10; i++) {
            manager.getTaskById(9);
        }

        manager.getTaskById(10);

        manager.updateTask(new Epic(9, "Updated Epic", "testing get history method"));

        System.out.println("Последние просмотренные задачи: " + manager.getHistory());

        //Удаляем все оставшиеся задачи
        System.out.println("\nУдаляем все оставшиеся задачи");

        System.out.println("Список оставшихся задача: " + manager.getAllTasks());

        manager.removeAllTasks();

        System.out.println("Список задач после удаления: " + manager.getAllTasks());
    }
}
