package main.java.managers;

import main.java.exceptions.*;
import main.java.repository.TaskRepository;
import main.java.tasks.*;

import java.io.*;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;
    private static final String HEADER = "id,type,name,status,description,epic";

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
            for (Task task : getAllTasks()) {
                bw.write(task.taskToString() + System.lineSeparator());
            }

            bw.write(System.lineSeparator() + historyToString());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении задач");
        }
    }

    private void taskFromString(String str) {
        String[] data = str.split(",");
        switch (Type.getType(data[1])) {
            case SUB:
                loadSubTask(Integer.parseInt(data[0]), data[2], data[4], Status.getStatus(data[3]), Integer.parseInt(data[5]));
                break;
            case EPIC:
                loadEpic(Integer.parseInt(data[0]), data[2], data[4]);
                break;
            case SINGLE:
                loadSingleTask(Integer.parseInt(data[0]), data[2], data[4], Status.getStatus(data[3]));
                break;
        }
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

    private SubTask loadSubTask(int id, String name, String description, Status status, int epicId) {
        try {
            Epic epic = (Epic) getTaskRepository().getTaskById(epicId);

            SubTask subTask = new SubTask(
                    id,
                    name,
                    description,
                    status,
                    epicId
            );

            getTaskRepository().saveTask(subTask);

            getTaskRepository().addSubTaskInEpic(epicId, subTask);

            save();

            return subTask;

        } catch (TaskNotFoundException | ClassCastException e) {
            throw new EpicNotFoundException(epicId);
        }
    }

    private Epic loadEpic(int id, String name, String description) {
        Epic epic = new Epic(
                id,
                name,
                description
        );

        getTaskRepository().saveTask(epic);

        save();

        return epic;
    }

    private Task loadSingleTask(int id, String name, String description, Status status) {
        Task task = new Task(
                id,
                name,
                description,
                status
        );

        getTaskRepository().saveTask(task);

        save();

        return task;
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
            throw new ManagerDeleteException("Ошибка при очистке файла: " + file.getName());
        }
    }

    @Override
    public void removeTaskById(int id) throws TaskNotFoundException {
        super.removeTaskById(id);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = getTaskRepository().getTaskById(id);
        getHistoryManager().add(task);
        save();
        return task;
    }

    public void loadFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            while (!(line = br.readLine()).isBlank()) {
                taskFromString(line);
            }

            if ((line = br.readLine()) != null) {
                loadHistory(line);
            }
        } catch (IOException e) {
            throw new ManagerLoadException("Ошибка при создании задачи из файла: " + file.getName());
        }
    }

    private void loadHistory(String str) {
        String[] el = str.split(",");
        for (String i : el) {
            getHistoryManager().add(getTaskById(Integer.parseInt(i)));
        }
    }
}
