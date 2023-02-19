package main.java.managers;

import main.java.exceptions.EpicNotFoundException;
import main.java.exceptions.ManagerDeleteException;
import main.java.exceptions.ManagerSaveException;
import main.java.exceptions.TaskNotFoundException;
import main.java.repository.TaskRepository;
import main.java.tasks.Epic;
import main.java.tasks.SubTask;
import main.java.tasks.Task;
import main.java.tasks.TaskIdGeneration;

import java.io.*;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;
    private static final String HEADER = "id,type,name,status,description,epic";

    public FileBackedTasksManager(TaskIdGeneration taskIdGeneration, TaskRepository taskRepository, HistoryManager historyManager, File file) {
        super(taskIdGeneration, taskRepository, historyManager);
        this.file = file;
    }

    private void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(HEADER + System.lineSeparator());
            for (Task task : getAllTasks()) {
                bw.write(task.taskToString() + System.lineSeparator());
            }
            bw.write(System.lineSeparator() + historyToString());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении задач");
        }
    }

    @Override
    public Task createNewSingleTask(String name, String description) {
        Task task = super.createNewSingleTask(name, description);
        save();
        return task;
    }

    @Override
    public Epic createNewEpic(String name, String description) {
        Epic epic = super.createNewEpic(name, description);
        save();
        return epic;
    }

    @Override
    public SubTask createNewSubTask(String name, String description, int epicId) throws EpicNotFoundException {
        SubTask subTask = super.createNewSubTask(name, description, epicId);
        save();
        return subTask;
    }

    @Override
    public Task updateTask(Task task) throws TaskNotFoundException {
        Task t = super.updateTask(task);
        save();
        return t;
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        try {
            new FileWriter(file, false).close();
        } catch (IOException e) {
            throw new ManagerDeleteException("Ошибка при очистки файла: " + file.getName());
        }
    }

    @Override
    public void removeTaskById(int id) throws TaskNotFoundException {
        super.removeTaskById(id);
        save();
    }

    private String historyToString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Task task : getHistory()) {
            if (stringBuilder.length() == 0) {
                stringBuilder.append(task.getId());
            } else {
                stringBuilder.append(",").append(task.getId());
            }
        }
        return stringBuilder.toString();
    }
}
