package main.java.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Epic extends Task {
    private final HashMap<Integer, SubTask> subTasks;
    private LocalDateTime endTime;

    public Epic(int id, String name, String description, Duration duration, LocalDateTime startTime) {
        super(id, name, description, Status.NEW, duration, startTime);
        subTasks = new HashMap<>();
    }

    @Override
    public Type getType() {
        return Type.EPIC;
    }

    @Override
    public Status getStatus() {
        if (subTasks == null || areAllSubTaskNew()) {
            return Status.NEW;
        } else if (areAllSubTasksCompleted()) {
            return Status.DONE;
        } else {
            return Status.IN_PROGRESS;
        }
    }

    private boolean areAllSubTaskNew() {
        return subTasks.values().stream().allMatch(s -> s.getStatus() == Status.NEW);
    }

    private boolean areAllSubTasksCompleted() {
        return subTasks.values().stream().allMatch(s -> s.getStatus() == Status.DONE);
    }

    public List<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    private void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void addSubTask(int id, SubTask subTask) {
        subTasks.put(id, subTask);
        updateDuration(subTask, true);
        updateStartTime(subTask, true);
        updatedEndTime(subTask, true);
    }

    private void updateStartTime(SubTask subTask, boolean isAdd) {
        if (isAdd) {
            if (getStartTime() != null) {
                setStartTime(
                        getStartTime().isBefore(subTask.getStartTime()) ? getStartTime() : subTask.getStartTime()
                );
            } else {
                setStartTime(subTask.getStartTime());
            }
        } else {
            Optional<SubTask> task = subTasks.values()
                    .stream()
                    .min(Comparator.comparing(Task::getStartTime));

            if (task.isPresent()) {
                setStartTime(task.get().getStartTime());
            } else {
                setStartTime(null);
            }
        }
    }

    private void updateDuration(SubTask subTask, boolean isAdd) {
        if (isAdd) {
            setDuration(getDuration().plus(subTask.getDuration()));
        } else {
            setDuration(getDuration().minus(subTask.getDuration()));
        }
    }

    private void updatedEndTime(SubTask subTask, boolean isAdd) {
        if (isAdd) {
            if (getEndTime() != null) {
                setEndTime(getEndTime().isBefore(subTask.getEndTime()) ? subTask.getEndTime() : getEndTime());
            } else {
                setEndTime(subTask.getEndTime());
            }
        } else {
            Optional<SubTask> task = subTasks.values()
                    .stream()
                    .max(Comparator.comparing(Task::getEndTime));

            if (task.isPresent()) {
                setEndTime(task.get().getEndTime());
            } else {
                setEndTime(LocalDateTime.MAX);
            }
        }
    }

    public void removeSubTask(int id) {
        SubTask subTask = subTasks.get(id);
        subTasks.remove(id);

        updateDuration(subTask, false);
        updateStartTime(subTask, false);
        updatedEndTime(subTask, false);
    }

    public void removeAllSubTasks() {
        subTasks.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return subTasks.equals(epic.subTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasks);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", descriptionLength='" + getDescription().length() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", numberOfSubTask='" + getSubTasks().size() + '\'' +
                ", duration='" + getDuration().toMinutes() + '\'' +
                ", startTime='" + getStartTime() + '\'' +
                ", endTime='" + getEndTime() + '\'' +
                '}';
    }
}
