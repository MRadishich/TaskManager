package main.java.manager;

import main.java.exceptions.TaskNotFoundException;
import main.java.repository.TaskRepository;
import main.java.tasks.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryTaskManager implements HistoryManager {
    public static final int NUMBER_OF_RECENTLY_VIEWED_TASKS = 10;

    private final TaskRepository taskRepository;
    private final LinkedList<Integer> viewedTasks;

    public InMemoryHistoryTaskManager(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.viewedTasks = new LinkedList<>();
    }

    @Override
    public void add(Task task) {
        viewedTasks.addFirst(task.getId());
    }

    //Оставил обращение к taskRepository, чтобы получать задачи в актуальном состоянии.
    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();

        for (int i = 0; i < viewedTasks.size() && history.size() < NUMBER_OF_RECENTLY_VIEWED_TASKS; i++) {
            try {
                history.add(taskRepository.getTaskById(viewedTasks.get(i)));
            } catch (TaskNotFoundException ignored) {
            }
        }

        return history;
    }
}
