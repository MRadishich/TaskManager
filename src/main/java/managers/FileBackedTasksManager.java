package main.java.managers;

import main.java.exceptions.EpicNotFoundException;
import main.java.repository.TaskRepository;
import main.java.tasks.Epic;
import main.java.tasks.SubTask;
import main.java.tasks.Task;
import main.java.tasks.TaskIdGeneration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTasksManager(TaskIdGeneration taskIdGeneration, TaskRepository taskRepository, HistoryManager historyManager, File file) {
        super(taskIdGeneration, taskRepository, historyManager);
        this.file = file;
    }

    private void save(Task task) {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(task.toStringForSaveInFile() + "\n");
        } catch (IOException e) {
            System.out.println("При сохранении задачи с id: " + task.getId() + "возникла ошибка");
        }
    }

    @Override
    public Task createNewSingleTask(String name, String description) {
        Task task = super.createNewSingleTask(name, description);
        save(task);
        return task;
    }

    @Override
    public Epic createNewEpic(String name, String description) {
        Epic epic = super.createNewEpic(name, description);
        save(epic);
        return epic;
    }

    @Override
    public SubTask createNewSubTask(String name, String description, int epicId) throws EpicNotFoundException {
        SubTask subTask = super.createNewSubTask(name, description, epicId);
        save(subTask);
        return subTask;
    }


}
