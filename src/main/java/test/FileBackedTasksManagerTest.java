package main.java.test;

import main.java.managers.Managers;
import main.java.managers.TaskManager;
import main.java.tasks.Status;
import main.java.tasks.SubTask;

import java.io.File;

public class FileBackedTasksManagerTest {
    public static void main(String[] args) {
        TaskManager manager = Managers.getFileBackedTasksManager(new File("taskBackup.csv"));

        manager.removeAllTasks();

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            manager.createNewEpic("Epic with id: " + i, "Epic for big Tasks");
            manager.createNewEpic("Epic with id: " + i, "Epic for small Tasks");
            manager.createNewSubTask("SubTask with id: " + i, "SubTask by big Epic", 0);
            manager.createNewSubTask("SubTask with id: " + i, "SubTask by small Epic", 1);
        }

        long endTime = System.currentTimeMillis();

        System.out.println("Время затраченное на создание задач: " + (endTime - startTime));

        for (int i = 0; i < 4000; i += 100) {
            manager.getTaskById(i);
        }

        startTime = System.currentTimeMillis();

        for (int i = 0; i < 4000; i += 2) {
            if (i % 6 == 0 && i != 0) {
                SubTask subTask = new SubTask(i,"Updated SubTask", "Updated SubTask", Status.DONE, i - 1);
                manager.updateTask(subTask);
            } else {
                manager.removeTaskById(i);
            }
        }

        endTime = System.currentTimeMillis();

        System.out.println("Время затраченное на удаление задач: " + (endTime - startTime));

    }
}
