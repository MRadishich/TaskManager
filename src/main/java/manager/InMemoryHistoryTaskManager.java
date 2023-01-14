package main.java.manager;

import main.java.tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryTaskManager implements HistoryManager {
    public static final int NUMBER_OF_RECENTLY_VIEWED_TASKS = 10;
    private final LinkedList<Task> viewedTasks = new LinkedList<>();


    @Override
    public void add(Task task) {
        viewedTasks.add(task);

        if (viewedTasks.size() > NUMBER_OF_RECENTLY_VIEWED_TASKS) {
            viewedTasks.removeFirst();
        }
    }

    @Override
    public List<Task> getHistory() {
        return viewedTasks;
    }
}
