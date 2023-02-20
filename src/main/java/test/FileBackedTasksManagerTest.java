package main.java.test;

import main.java.managers.FileBackedTasksManager;
import main.java.managers.Managers;
import main.java.tasks.Status;
import main.java.tasks.SubTask;

import java.io.File;

public class FileBackedTasksManagerTest {
    public static void main(String[] args) {
        write();
        read();
    }

    public static void write() {
        File file = new File("taskBackup.csv");
        FileBackedTasksManager manager = Managers.getFileBackedTasksManager(file);
        manager.removeAllTasks();

        manager.createNewEpic("Epic #1", "Epic for big Tasks");
        manager.createNewEpic("Epic #2", "Epic for small Tasks");
        manager.createNewEpic("Epic #3", "Epic for other Tasks");
        manager.createNewSubTask("SubTask #1", "SubTask by Epic #1", 0);
        manager.createNewSubTask("SubTask #1", "SubTask by Epic #2", 1);
        manager.createNewSubTask("SubTask #2", "SubTask by Epic #2", 1);
        manager.createNewSubTask("SubTask #3", "SubTask by Epic #2", 1);
        manager.createNewSubTask("SubTask #1", "SubTask by Epic #3", 2);
        manager.createNewSubTask("SubTask #2", "SubTask by Epic #3", 2);

        SubTask subTask = new SubTask(7, "SubTask #1", "SubTask by Epic #1", Status.DONE, 2);
        manager.updateTask(subTask);

        manager.getTaskById(0);
        manager.getTaskById(1);
        manager.getTaskById(2);
        manager.getTaskById(3);
        manager.getTaskById(4);

        manager.removeTaskById(1);
    }

    public static void read() {
        File file = new File("taskBackup.csv");
        FileBackedTasksManager manager = Managers.getFileBackedTasksManager(file);
        manager.loadFromFile();
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getHistory());

        SubTask subTask = new SubTask(8, "SubTask #1", "SubTask by Epic #1", Status.DONE, 2);
        manager.updateTask(subTask);

        System.out.println(manager.getTaskById(2).getStatus());
    }
}
