package main.java.managers;

import main.java.exceptions.*;
import main.java.repository.TaskRepository;
import main.java.tasks.*;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;
    private static final String HEADER = "id,type,name,status,description,epic";
    private static final String FIELD_SEPARATOR = ",";

    public FileBackedTasksManager(TaskIdGeneration taskIdGeneration, TaskRepository taskRepository, HistoryManager historyManager, File file) {
        super(taskIdGeneration, taskRepository, historyManager);
        this.file = file;
    }

    @Override
    public SubTask createNewSubTask(String name, String description, int epicId) throws EpicNotFoundException {
        SubTask subTask = super.createNewSubTask(name, description, epicId);
        save();
        return subTask;
    }

    @Override
    public Epic createNewEpic(String name, String description) {
        Epic epic = new Epic(
                getTaskIdGeneration().getNextFreeId(),
                name,
                description
        );

        getTaskRepository().saveTask(epic);

        save();

        return epic;
    }

    @Override
    public Task createNewSingleTask(String name, String description) {
        Task task = super.createNewSingleTask(name, description);
        save();
        return task;
    }

    private void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(HEADER + System.lineSeparator());

            bw.write(getAllTasks().stream()
                    .map(Task::taskToString)
                    .collect(Collectors.joining(System.lineSeparator())));

            bw.write(System.lineSeparator() + System.lineSeparator() + historyToString());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении задач");
        }
    }

    private String historyToString() {
        return getHistory().stream()
                .map(Task::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(FIELD_SEPARATOR));
    }

    private Task taskFromString(String line) throws IllegalArgumentException {
        String[] taskByField = line.split(FIELD_SEPARATOR);
        switch (Type.valueOf(taskByField[1])) {
            case SUB:
                return new SubTask(
                        Integer.parseInt(taskByField[0]),
                        taskByField[2], taskByField[4],
                        Status.valueOf(taskByField[3]),
                        Integer.parseInt(taskByField[5])
                );
            case EPIC:
                return new Epic(
                        Integer.parseInt(taskByField[0]),
                        taskByField[2],
                        taskByField[4]
                );
            case SINGLE:
                return new Task(
                        Integer.parseInt(taskByField[0]),
                        taskByField[2],
                        taskByField[4],
                        Status.valueOf(taskByField[3])
                );
        }

        return null;
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
            new FileWriter(file).close();
        } catch (IOException e) {
            throw new ManagerDeleteException("Ошибка при удалении задач из файла: " + file.getName());
        }
    }

    @Override
    public void removeTaskById(int id) throws TaskNotFoundException {
        super.removeTaskById(id);
        save();
    }

    @Override
    public Task getTaskById(int id) throws TaskNotFoundException {
        Task task = getTaskRepository().getTaskById(id);
        getHistoryManager().add(task);
        save();
        return task;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager manager = Managers.getFileBackedTasksManager(file);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();

            while (!(line = br.readLine()).isBlank()) {
                Task task = manager.taskFromString(line);
                manager.getTaskRepository().saveTask(task);
            }

            if ((line = br.readLine()) != null) {
                manager.historyFromString(line).forEach(
                        i -> manager.getHistoryManager()
                                .add(manager.getTaskById(i))
                );
            }
        } catch (IOException e) {
            throw new ManagerLoadException("Ошибка при создании задачи из файла: " + file.getName());
        }

        return manager;
    }

    private List<Integer> historyFromString(String line) throws TaskNotFoundException {
        return Arrays.stream(line.split(FIELD_SEPARATOR))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
