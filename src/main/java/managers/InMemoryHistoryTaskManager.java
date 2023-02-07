package main.java.managers;

import main.java.customCollections.CustomLinkedList;
import main.java.repository.TaskRepository;
import main.java.tasks.Task;

import java.util.*;

public class InMemoryHistoryTaskManager implements HistoryManager {
    private final TaskRepository taskRepository;
    private final CustomLinkedList<Integer> viewedTasks;

    public InMemoryHistoryTaskManager(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.viewedTasks = new CustomLinkedList<>();
    }

    @Override
    public void add(Task task) {
        viewedTasks.add(task.getId());
    }

    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();

        for (Integer i : viewedTasks) {
            history.add(taskRepository.getTaskById(i));
        }

        return history;
    }

    @Override
    public void remove(int id) {
        viewedTasks.remove(id);
    }

    @Override
    public void clear() {
        viewedTasks.clear();
    }
}

