package main.java.repository;

import main.java.exceptions.EpicNotFoundException;
import main.java.exceptions.TaskNotFoundException;
import main.java.tasks.*;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskRepository implements TaskRepository {
    private final Map<Integer, Task> tasks;
    private final Set<Task> taskByPriority;
    private final TaskValidator taskValidator;

    public InMemoryTaskRepository() {
        this.tasks = new HashMap<>();
        this.taskValidator = new TaskValidator();
        this.taskByPriority = new TreeSet<>(Comparator.comparing(Task::getStartTime).thenComparing(Task::getId));
    }

    @Override
    public void saveTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            removeTaskById(task.getId());
        }

        if (!taskValidator.checkTask(task)) {
            throw new RuntimeException("Невозможно создать задачу \"" + task.getName() + "\" т.к. она пересекается с другой задачей.");
        }

        tasks.put(task.getId(), task);

        if (!(task instanceof Epic)) {
            taskByPriority.add(task);
        }

        if (task instanceof SubTask) {
            Epic epic = (Epic) getTaskById(((SubTask) task).getEpicId());
            epic.addSubTask((SubTask) task);
        }
    }

    @Override
    public Task getTaskById(int id) {
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
    public List<SubTask> getAllSubTasksByEpicId(int id) {
        if (!(tasks.get(id) instanceof Epic)) {
            throw new EpicNotFoundException(id);
        }

        Epic epic = (Epic) tasks.get(id);

        return epic.getSubTasks();
    }

    @Override
    public List<Task> getAllTaskByPriority() {
        return new ArrayList<>(taskByPriority);
    }

    @Override
    public Task updateTask(Task task) {
        int taskId = task.getId();

        if (!tasks.containsKey(taskId)) {
            throw new TaskNotFoundException(taskId);
        } else {
            if (tasks.get(task.getId()) instanceof SubTask) {
                updateListSubTasks((SubTask) tasks.get(taskId), (SubTask) task);
            }

            taskValidator.checkTask(task);
            if (!(task instanceof Epic)) {
                updateTaskByPriority(tasks.get(taskId), task);
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

        epic.removeSubTask(oldSubTask);
        epic.addSubTask(newSubTask);
    }

    private void updateTaskByPriority(Task oldTask, Task newTask) {
        taskByPriority.remove(oldTask);
        taskByPriority.add(newTask);
    }

    private boolean isEpicIdEquals(SubTask oldTask, SubTask newTask) {
        return oldTask.getEpicId() == newTask.getEpicId();
    }

    private void removeOldSubTask(SubTask oldSubTask) {
        Epic oldEpic = (Epic) tasks.get(oldSubTask.getEpicId());

        oldEpic.removeSubTask(oldSubTask);
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
        taskByPriority.clear();
        taskValidator.clear();
    }

    @Override
    public void removeTaskById(int id) throws TaskNotFoundException {
        if (!tasks.containsKey(id)) {
            throw new TaskNotFoundException(id);
        } else {
            Task task = tasks.get(id);
            if (task instanceof SubTask) {
                removeSubTaskInEpic((SubTask) task);
            } else if (task instanceof Epic) {
                changeTypeSubTasks((Epic) task);
            }
            tasks.remove(id);
            taskValidator.releaseInterval(task);

            if (!(task instanceof Epic)) {
                taskByPriority.remove(task);
            }
        }
    }

    private void removeSubTaskInEpic(SubTask subTask) {
        Epic epic = (Epic) tasks.get(subTask.getEpicId());

        epic.removeSubTask(subTask);
    }

    private void changeTypeSubTasks(Epic epic) {
        for (SubTask subTask : epic.getSubTasks()) {
            tasks.put(subTask.getId(), new Task(
                    subTask.getId(),
                    subTask.getName(),
                    subTask.getDescription(),
                    subTask.getStatus(),
                    subTask.getDuration(),
                    subTask.getStartTime()
            ));
        }

        epic.removeAllSubTasks();
    }
}
