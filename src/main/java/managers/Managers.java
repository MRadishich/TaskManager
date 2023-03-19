package main.java.managers;

import main.java.managers.filebacked.FileBackedTasksManager;
import main.java.managers.http.HttpTaskManager;
import main.java.managers.inmemory.InMemoryHistoryTaskManager;
import main.java.managers.inmemory.InMemoryTaskManager;
import main.java.repository.InMemoryTaskRepository;
import main.java.repository.TaskRepository;
import main.java.tasks.TaskIdGeneration;

public class Managers {
    private static InMemoryTaskRepository inMemoryTaskRepository;
    private static InMemoryHistoryTaskManager inMemoryHistoryTaskManager;
    private static InMemoryTaskManager inMemoryTaskManager;
    private static FileBackedTasksManager fileBackedTasksManager;
    private static HttpTaskManager httpTaskManager;

    public static TaskRepository getDefaultTaskRepository() {
        if (inMemoryTaskRepository == null) {
            inMemoryTaskRepository = new InMemoryTaskRepository();
        }

        return inMemoryTaskRepository;
    }

    public static HistoryManager getDefaultHistoryManager() {
        if (inMemoryHistoryTaskManager == null) {
            inMemoryHistoryTaskManager = new InMemoryHistoryTaskManager(getDefaultTaskRepository());
        }

        return inMemoryHistoryTaskManager;
    }

    public static TaskManager getDefaultTaskManager(String url) {
        if (httpTaskManager == null) {
            httpTaskManager = new HttpTaskManager(
                    new TaskIdGeneration(),
                    getDefaultTaskRepository(),
                    getDefaultHistoryManager(),
                    url
            );
        }

        return httpTaskManager;
    }

    public static FileBackedTasksManager getFileBackedTasksManager() {
        if (fileBackedTasksManager == null) {
            fileBackedTasksManager = new FileBackedTasksManager(
                    new TaskIdGeneration(),
                    getDefaultTaskRepository(),
                    getDefaultHistoryManager()
            );
        }

        return fileBackedTasksManager;
    }

    public static InMemoryTaskManager getInMemoryTaskManager() {
        if (inMemoryTaskManager == null) {
            inMemoryTaskManager = new InMemoryTaskManager(
                    new TaskIdGeneration(),
                    getDefaultTaskRepository(),
                    getDefaultHistoryManager()
            );
        }

        return inMemoryTaskManager;
    }
}
