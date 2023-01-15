package main.java.manager;

import main.java.repository.InMemoryTaskRepository;
import main.java.tasks.TaskIdGeneration;

public class Managers {
    private static TaskIdGeneration taskIdGeneration;
    private static InMemoryTaskRepository inMemoryTaskRepository;
    private static InMemoryHistoryTaskManager inMemoryHistoryTaskManager;
    private static InMemoryTaskManager inMemoryTaskManager;

    private static TaskIdGeneration getTaskIdGeneration() {
        if (taskIdGeneration == null) {
            taskIdGeneration = new TaskIdGeneration();
        }

        return taskIdGeneration;
    }

    private static InMemoryTaskRepository getInMemoryTaskRepository() {
        if (inMemoryTaskRepository == null) {
            inMemoryTaskRepository = new InMemoryTaskRepository();
        }

        return inMemoryTaskRepository;
    }

    private static InMemoryHistoryTaskManager getInMemoryHistoryTaskManager() {
        if (inMemoryHistoryTaskManager == null) {
            inMemoryHistoryTaskManager = new InMemoryHistoryTaskManager(getInMemoryTaskRepository());
        }

        return inMemoryHistoryTaskManager;
    }

    private static InMemoryTaskManager getInMemoryTaskManager() {
        if (inMemoryTaskManager == null) {
            inMemoryTaskManager = new InMemoryTaskManager(
                    getTaskIdGeneration(),
                    getInMemoryTaskRepository(),
                    getInMemoryHistoryTaskManager()
            );
        }

        return inMemoryTaskManager;
    }

    public static TaskManager getDefaultTaskManager() {
        return getInMemoryTaskManager();
    }
 }
