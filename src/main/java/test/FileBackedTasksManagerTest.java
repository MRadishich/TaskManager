package main.java.test;

import main.java.managers.Managers;
import main.java.managers.TaskManager;

import java.io.File;

public class FileBackedTasksManagerTest {
    public static void main(String[] args) {
        TaskManager manager = Managers.getFileBackedTasksManager(new File("taskBackup.csv"));

        manager.removeAllTasks();

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            manager.createNewEpic("Epic #1", "Epic #1");
            manager.createNewSubTask("SubTask #1", "SubTask #1", 0);
            manager.createNewSubTask("SubTask #1", "SubTask #1", 0);
        }

        long endTime = System.currentTimeMillis();

        System.out.println("Время затраченное на создание задач: " + (endTime - startTime));

        startTime = System.currentTimeMillis();

        for (int i = 0; i < 3000; i += 10) {
            manager.removeTaskById(i);
        }

        endTime = System.currentTimeMillis();

        System.out.println("Время затраченное на удаление задач: " + (endTime - startTime));
    }
}
