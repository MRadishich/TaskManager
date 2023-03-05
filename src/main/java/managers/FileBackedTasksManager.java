package main.java.managers;

import main.java.dto.TaskDTO;
import main.java.exceptions.ManagerDeleteException;
import main.java.exceptions.ManagerLoadException;
import main.java.exceptions.ManagerSaveException;
import main.java.exceptions.TaskNotFoundException;
import main.java.repository.TaskRepository;
import main.java.tasks.Status;
import main.java.tasks.Task;
import main.java.tasks.TaskIdGeneration;
import main.java.tasks.Type;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
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
        TaskDTO taskDTO = new TaskDTO(
                Integer.parseInt(taskByField[0]),
                Type.valueOf(taskByField[1]),
                taskByField[2],
                taskByField[3],
                Status.valueOf(taskByField[4]),
                Duration.ofMinutes(Long.parseLong(taskByField[5])),
                LocalDateTime.parse(taskByField[6]),
                taskByField.length == 8 ? Integer.parseInt(taskByField[7]) : null
        );
        return createTask(taskDTO);
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
        int maxId = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();

            while (!(line = br.readLine()).isBlank()) {
                Task task = manager.taskFromString(line);
                manager.getTaskRepository().saveTask(task);
                maxId = Math.max(maxId, task.getId());

            }

            if ((line = br.readLine()) != null) {
                manager.historyFromString(line).forEach(
                        i -> manager.getHistoryManager()
                                .add(manager.getTaskById(i))
                );
            }
            manager.getTaskIdGeneration().setNextFreeId(++maxId);
        } catch (IOException e) {
            throw new ManagerLoadException("Ошибка чтения файла: " + file.getName());
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new RuntimeException(e.getMessage());
        }

        return manager;
    }

    private List<Integer> historyFromString(String line) throws TaskNotFoundException {
        return Arrays.stream(line.split(FIELD_SEPARATOR))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
