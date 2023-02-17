package main.java.test;

import main.java.managers.Managers;
import main.java.managers.TaskManager;

public class FileBackedTasksManagerTest {
    public static void main(String[] args) {
        TaskManager manager = Managers.getFileBackedTasksManager();
        manager.createNewEpic("Epic #1", "Epic #1");
        manager.createNewSubTask("SubTask #1", "SubTask #1", 0);
    }
}
