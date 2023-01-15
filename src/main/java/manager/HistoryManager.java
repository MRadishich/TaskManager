package main.java.manager;

import main.java.tasks.Task;

import java.util.List;

public interface HistoryManager {

    void add(Integer id);

    List<Task> getHistory();

}
