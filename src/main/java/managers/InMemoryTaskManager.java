package main.java.managers;

import main.java.exceptions.EpicNotFoundException;
import main.java.exceptions.TaskNotFoundException;
import main.java.repository.TaskRepository;
import main.java.tasks.*;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final TaskIdGeneration taskIdGeneration;
    private final HistoryManager historyManager;
    private final TaskRepository taskRepository;

    public InMemoryTaskManager(
            TaskIdGeneration taskIdGeneration,
            TaskRepository taskRepository,
            HistoryManager historyManager) {
        this.taskIdGeneration = taskIdGeneration;
        this.taskRepository = taskRepository;
        this.historyManager = historyManager;
    }

    protected TaskIdGeneration getTaskIdGeneration() {
        return taskIdGeneration;
    }

    protected HistoryManager getHistoryManager() {
        return historyManager;
    }

    protected TaskRepository getTaskRepository() {
        return taskRepository;
    }

    @Override
    public Task createNewSingleTask(String name, String description) {
        Task task = new Task(
                taskIdGeneration.getNextFreeId(),
                name,
                description
        );

        taskRepository.saveTask(task);

        return task;
    }

    @Override
    public Epic createNewEpic(String name, String description) {
        Epic epic = new Epic(
                taskIdGeneration.getNextFreeId(),
                name,
                description
        );

        taskRepository.saveTask(epic);

        return epic;
    }

    @Override
    public SubTask createNewSubTask(String name, String description, int epicId) throws EpicNotFoundException {
        try {
            Epic epic = (Epic) taskRepository.getTaskById(epicId);

            SubTask subTask = new SubTask(
                    taskIdGeneration.getNextFreeId(),
                    name,
                    description,
                    Status.NEW,
                    epicId
            );

            taskRepository.saveTask(subTask);

            taskRepository.addSubTaskInEpic(epicId, subTask);

            return subTask;

        } catch (TaskNotFoundException | ClassCastException e) {
            throw new EpicNotFoundException(epicId);
        }
    }

    @Override
    public Task updateTask(Task task) throws TaskNotFoundException {
        return taskRepository.updateTask(task);
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(taskRepository.getAllTasks());
    }

    @Override
    public Task getTaskById(int id) throws TaskNotFoundException {
        Task task = taskRepository.getTaskById(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public List<Task> getAllSingleTasks() {
        return taskRepository.getAllSingleTasks();
    }

    @Override
    public List<Task> getAllEpic() {
        return taskRepository.getAllEpic();
    }

    @Override
    public List<Task> getAllSubTasks() {
        return taskRepository.getAllSubTasks();
    }

    @Override
    public List<SubTask> getAllSubTasksByEpicId(int id) throws EpicNotFoundException {
        return taskRepository.getAllSubTasksByEpicId(id);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void removeAllTasks() {
        taskRepository.removeAllTasks();
        historyManager.clear();
    }

    @Override
    public void removeTaskById(int id) throws TaskNotFoundException {
        taskRepository.removeTaskById(id);
        historyManager.remove(id);
    }
}
