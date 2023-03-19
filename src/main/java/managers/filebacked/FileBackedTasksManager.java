package main.java.managers.filebacked;

import main.java.dto.TaskDTO;
import main.java.exceptions.ManagerDeleteException;
import main.java.exceptions.ManagerLoadException;
import main.java.exceptions.ManagerSaveException;
import main.java.exceptions.TaskNotFoundException;
import main.java.managers.HistoryManager;
import main.java.managers.Managers;
import main.java.managers.inmemory.InMemoryTaskManager;
import main.java.repository.TaskRepository;
import main.java.tasks.Epic;
import main.java.tasks.SubTask;
import main.java.tasks.Task;
import main.java.tasks.TaskIdGeneration;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File fileToSave;
    private static final String HEADER = "id,type,name,description,status,duration,startTime,epicId";
    private static final String FIELD_SEPARATOR = ",";

    public FileBackedTasksManager(TaskIdGeneration taskIdGeneration, TaskRepository taskRepository, HistoryManager historyManager) {
        super(taskIdGeneration, taskRepository, historyManager);
        this.fileToSave = new File("saved.csv");
    }

    protected void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileToSave))) {
            bw.write(HEADER + System.lineSeparator());

            bw.write(getAllTasks().stream()
                    .map(TaskDTO::toTaskDTO)
                    .map(TaskDTO::asString)
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
        return createTask(TaskDTO.getTaskDTO(line, FIELD_SEPARATOR));
    }

    @Override
    public Task createTask(TaskDTO taskDTO) {
        switch (taskDTO.getType()) {
            case SINGLE:
                Task task = new Task(
                        taskDTO.getId() == null ? getTaskIdGeneration().getNextFreeId() : taskDTO.getId(),
                        taskDTO.getName(),
                        taskDTO.getDescription(),
                        taskDTO.getStatus(),
                        taskDTO.getDuration(),
                        taskDTO.getStartTime()
                );
                getTaskRepository().saveTask(task);
                save();
                return task;
            case EPIC:
                Epic epic = new Epic(
                        taskDTO.getId() == null ? getTaskIdGeneration().getNextFreeId() : taskDTO.getId(),
                        taskDTO.getName(),
                        taskDTO.getDescription(),
                        taskDTO.getDuration(),
                        taskDTO.getStartTime()
                );
                getTaskRepository().saveTask(epic);
                save();
                return epic;
            case SUB:
                SubTask subTask = new SubTask(
                        taskDTO.getId() == null ? getTaskIdGeneration().getNextFreeId() : taskDTO.getId(),
                        taskDTO.getName(),
                        taskDTO.getDescription(),
                        taskDTO.getStatus(),
                        taskDTO.getDuration(),
                        taskDTO.getStartTime(),
                        taskDTO.getEpicId()
                );
                getTaskRepository().saveTask(subTask);
                save();
                return subTask;
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: '" + taskDTO.getType() + "'");
        }
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
            new FileWriter(fileToSave, false).close();
        } catch (IOException e) {
            throw new ManagerDeleteException("Ошибка при удалении задач из файла: " + fileToSave.getName());
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

    public static FileBackedTasksManager loadFromFile(File fileToRead) {
        FileBackedTasksManager manager = Managers.getFileBackedTasksManager();

        if (fileToRead.length() == 0) {
            return manager;
        }

        int maxId = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(fileToRead))) {
            String line = br.readLine();

            while ((line = br.readLine()) != null) {
                if (line.isBlank()) {
                    break;
                }
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
            throw new ManagerLoadException("Ошибка чтения файла: " + fileToRead.getName());
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new RuntimeException(e.getMessage());
        }

        return manager;
    }

    public File getSavedData() {
        return fileToSave;
    }

    private List<Integer> historyFromString(String line) throws TaskNotFoundException {
        return Arrays.stream(line.split(FIELD_SEPARATOR))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
