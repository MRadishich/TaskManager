package main.java.tests;

import main.java.managers.HistoryManager;
import main.java.managers.Managers;
import main.java.managers.TaskManager;

public class CustomLinkedListTest {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefaultTaskManager();
        HistoryManager historyManager = Managers.getDefaultHistoryManager();

        taskManager.createNewEpic("Epic №1", "Epic number one with three subTasks");
        taskManager.createNewSubTask("SubTask №1", "SubTask number one", 0);
        taskManager.createNewSubTask("SubTask №1", "SubTask number two", 0);
        taskManager.createNewSubTask("SubTask №1", "SubTask number three", 0);
        taskManager.createNewEpic("Epic №2", "Epic number two without subTasks");

        System.out.println(taskManager.getTaskById(0));
        System.out.println(taskManager.getTaskById(0));
        System.out.println(taskManager.getTaskById(1));
        System.out.println(taskManager.getTaskById(1));
        System.out.println(taskManager.getTaskById(2));
        System.out.println(taskManager.getTaskById(4));
        System.out.println(taskManager.getTaskById(4));
        System.out.println(taskManager.getTaskById(0));
        System.out.println(taskManager.getTaskById(2));
        System.out.println(taskManager.getTaskById(3));

        System.out.println(historyManager.getHistory());

        taskManager.removeTaskById(0);

        System.out.println(historyManager.getHistory());

        taskManager.removeAllTasks();

        System.out.println(historyManager.getHistory());
    }
}
