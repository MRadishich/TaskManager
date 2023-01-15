package main.java.repository;

import main.java.tasks.SubTask;
import main.java.tasks.Task;

import java.util.List;

public interface TaskRepository {

    void saveTask(Task task);

    void addSubTaskInEpic(int epicId, SubTask subTask);

    Task getTaskById(int id);

    List<Task> getAllTasks();

    List<Task> getAllSingleTasks();

    List<Task> getAllEpic();

    List<Task> getAllSubTasks();

    List<SubTask> getAllSubTasksByEpicId(int id);

    Task updateTask(Task task);

    void removeAllTasks();

    void removeTaskById(int id);
}
