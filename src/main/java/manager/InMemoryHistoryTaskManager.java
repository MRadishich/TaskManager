package main.java.manager;

import main.java.repository.InMemoryTaskRepository;
import main.java.tasks.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryTaskManager implements HistoryManager {
    public static final int NUMBER_OF_RECENTLY_VIEWED_TASKS = 10;
    private final InMemoryTaskRepository taskRepository;
    private final LinkedList<Integer> viewedTasks;

    public InMemoryHistoryTaskManager(InMemoryTaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.viewedTasks = new LinkedList<>();
    }


    @Override
    public void add(Integer id) {
        viewedTasks.add(id);

        if (viewedTasks.size() > NUMBER_OF_RECENTLY_VIEWED_TASKS) {
            viewedTasks.removeFirst();
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> recentlyViewedTask = new ArrayList<>(viewedTasks.size());
        for (Integer id : viewedTasks) {
            recentlyViewedTask.add(taskRepository.getTaskById(id));
        }
        return recentlyViewedTask;
    }
}
