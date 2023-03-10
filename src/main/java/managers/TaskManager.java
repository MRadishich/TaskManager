package main.java.managers;

import main.java.dto.TaskDTO;
import main.java.exceptions.EpicNotFoundException;
import main.java.exceptions.TaskNotFoundException;
import main.java.tasks.SubTask;
import main.java.tasks.Task;

import java.util.List;

public interface TaskManager {
    Task createTask(TaskDTO taskDTO);

    Task updateTask(Task task) throws TaskNotFoundException;

    List<Task> getAllTasks();

    Task getTaskById(int id) throws TaskNotFoundException;

    List<Task> getAllSingleTasks();

    List<Task> getAllEpic();

    List<Task> getAllSubTasks();

    List<Task> getAllTaskByPriority();

    List<SubTask> getAllSubTasksByEpicId(int id) throws EpicNotFoundException;

    List<Task> getHistory();

    void removeAllTasks();

    void removeTaskById(int id) throws TaskNotFoundException;
}
