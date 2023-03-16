package main.java.managers.inmemory;

import main.java.dto.TaskDTO;
import main.java.exceptions.EpicNotFoundException;
import main.java.exceptions.TaskNotFoundException;
import main.java.managers.HistoryManager;
import main.java.managers.TaskManager;
import main.java.repository.TaskRepository;
import main.java.tasks.Epic;
import main.java.tasks.SubTask;
import main.java.tasks.Task;
import main.java.tasks.TaskIdGeneration;

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
    public Task createTask(TaskDTO taskDTO) {
        switch (taskDTO.getType()) {
            case SINGLE:
                Task task = new Task(
                        taskDTO.getId() == null ? taskIdGeneration.getNextFreeId() : taskDTO.getId(),
                        taskDTO.getName(),
                        taskDTO.getDescription(),
                        taskDTO.getStatus(),
                        taskDTO.getDuration(),
                        taskDTO.getStartTime()
                );
                taskRepository.saveTask(task);
                return task;
            case EPIC:
                Epic epic = new Epic(
                        taskDTO.getId() == null ? taskIdGeneration.getNextFreeId() : taskDTO.getId(),
                        taskDTO.getName(),
                        taskDTO.getDescription(),
                        taskDTO.getDuration(),
                        taskDTO.getStartTime()
                );
                taskRepository.saveTask(epic);
                return epic;
            case SUB:
                SubTask subTask = new SubTask(
                        taskDTO.getId() == null ? taskIdGeneration.getNextFreeId() : taskDTO.getId(),
                        taskDTO.getName(),
                        taskDTO.getDescription(),
                        taskDTO.getStatus(),
                        taskDTO.getDuration(),
                        taskDTO.getStartTime(),
                        taskDTO.getEpicId()
                );
                taskRepository.saveTask(subTask);
                return subTask;
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: '" + taskDTO.getType() + "'");
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
    public List<Task> getAllTaskByPriority() {
        return taskRepository.getAllTaskByPriority();
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
