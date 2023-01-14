package main.java.manager;

import main.java.exceptions.EpicNotFoundException;
import main.java.exceptions.TaskNotFoundException;
import main.java.tasks.Epic;
import main.java.tasks.SubTask;
import main.java.tasks.Task;

import java.util.List;

public interface TaskManager {

    Task createNewSingleTask(String name, String description);

    Epic createNewEpic(String name, String description);

    SubTask createNewSubTask(String name, String description, int epicId) throws EpicNotFoundException;

    Task updateTask(Task task) throws TaskNotFoundException;

    List<Task> getAllTasks();

    Task getTaskById(int id) throws TaskNotFoundException;

    List<Task> getAllSingleTasks();

    List<Task> getAllEpic();

    List<Task> getAllSubTasks();

    List<SubTask> getAllSubTasksByEpicId(int id) throws EpicNotFoundException;

    List<Task> getHistory();

    void removeAllTasks();

    void removeTaskById(int id) throws TaskNotFoundException;


}
