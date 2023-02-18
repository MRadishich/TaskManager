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

    private void save(Task task) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            if (file.length() == 0) {
                bw.write(HEADER);
                bw.write(System.lineSeparator());
            }
            bw.write(task.toStringForSaveInFile());
            bw.write(System.lineSeparator());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении задачи с id: " + task.getId());
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

    @Override
    public Task updateTask(Task task) throws TaskNotFoundException {
        Task t = super.updateTask(task);
        save(task);
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
        delete(id);
    }

    private void delete(int id) {
        File tempFile = new File("tempFile");
        try (BufferedReader br = new BufferedReader(new FileReader(file));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile, false))) {
            bw.write(br.readLine());
            bw.write(System.lineSeparator());
            while (br.ready()) {
                String line = br.readLine();
                String[] arr = line.split(",");
                if (Integer.parseInt(arr[0]) == id) {
                    continue;
                }
                bw.write(line);
                bw.write(System.lineSeparator());
            }

            if (!tempFile.renameTo(file)) {
                throw new ManagerDeleteException("Ошибка при удалении задачи с id: " + id);
            }
        } catch (IOException e) {
            throw new ManagerDeleteException("Ошибка при удалении задачи с id: " + id);
        }
    }


}
