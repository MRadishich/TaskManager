package main.java.repository;

import main.java.exceptions.EpicNotFoundException;
import main.java.exceptions.TaskNotFoundException;
import main.java.tasks.Epic;
import main.java.tasks.SubTask;
import main.java.tasks.Task;
import main.java.tasks.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryTaskRepository implements TaskRepository {
    private final HashMap<Integer, Task> tasks;

    public InMemoryTaskRepository() {
        this.tasks = new HashMap<>();
    }

    @Override
    public void saveTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void addSubTaskInEpic(int epicId, SubTask subTask) {
        Epic epic = (Epic) tasks.get(epicId);

        epic.addSubTask(subTask.getId(), subTask);
    }

    @Override
    public Task getTaskById(int id) throws TaskNotFoundException {
        if (!tasks.containsKey(id)) {
            throw new TaskNotFoundException(id);
        } else {
            return tasks.get(id);
        }
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Task> getAllSingleTasks() {
        return tasks.values()
                .stream()
                .filter(task -> (task.getType() == Type.SINGLE))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<Task> getAllEpic() {
        return tasks.values()
                .stream()
                .filter(task -> task.getType() == Type.EPIC)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<Task> getAllSubTasks() {
        return tasks.values()
                .stream()
                .filter(task -> task.getType() == Type.SUB)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<SubTask> getAllSubTasksByEpicId(int id) throws EpicNotFoundException {
        if (!(tasks.get(id) instanceof Epic)) {
            throw new EpicNotFoundException(id);
        }

        Epic epic = (Epic) tasks.get(id);

        return epic.getSubTasks();
    }

    @Override
    public Task updateTask(Task task) throws TaskNotFoundException {
        int taskId = task.getId();

        if (!tasks.containsKey(taskId)) {
            throw new TaskNotFoundException(taskId);
        } else {
            if (tasks.get(task.getId()) instanceof SubTask) {
                updateListSubTasks((SubTask) tasks.get(taskId), (SubTask) task);
            }
            tasks.put(taskId, task);
            return task;
        }
    }

    private void updateListSubTasks(SubTask oldSubTask, SubTask newSubTask) {
        Epic epic = (Epic) tasks.get(newSubTask.getEpicId());

        if (!isEpicIdEquals(oldSubTask, newSubTask)) {
            removeOldSubTask(oldSubTask);
        }

        epic.addSubTask(newSubTask.getId(), newSubTask);
    }

    private boolean isEpicIdEquals(SubTask oldTask, SubTask newTask) {
        return oldTask.getEpicId() == newTask.getEpicId();
    }

    private void removeOldSubTask(SubTask oldSubTask) {
        Epic oldEpic = (Epic) tasks.get(oldSubTask.getEpicId());

        oldEpic.removeSubTask(oldSubTask.getId());
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    @Override
    public void removeTaskById(int id) throws TaskNotFoundException {
        if (!tasks.containsKey(id)) {
            throw new TaskNotFoundException(id);
        } else {
            if (tasks.get(id) instanceof SubTask) {
                removeSubTaskInEpic(id);
            } else if (tasks.get(id) instanceof Epic) {
                changeTypeSubTasks((Epic) tasks.get(id));
            }
            tasks.remove(id);
        }
    }

    private void removeSubTaskInEpic(int id) {
        SubTask subTask = (SubTask) tasks.get(id);
        Epic epic = (Epic) tasks.get(subTask.getEpicId());

        epic.removeSubTask(id);
    }

    private void changeTypeSubTasks(Epic epic) {
        for (SubTask subTask : epic.getSubTasks()) {
            tasks.put(subTask.getId(), new Task(
                    subTask.getId(),
                    subTask.getName(),
                    subTask.getDescription(),
                    subTask.getStatus())
            );
        }

        epic.removeAllSubTask();
    }
}
