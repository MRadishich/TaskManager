package main.java.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Epic extends Task {
    private final Set<SubTask> subTasks;
    public Epic(int id, String name, String description, Duration duration, LocalDateTime startTime) {
        super(id, name, description, Status.NEW, duration, startTime);
        subTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime,
                Comparator.nullsLast(Comparator.naturalOrder())));
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
        return subTasks.stream().allMatch(s -> s.getStatus() == Status.NEW);
    }

    private boolean areAllSubTasksCompleted() {
        return subTasks.stream().allMatch(s -> s.getStatus() == Status.DONE);
    }

    public List<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks);
    }

    @Override
    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    private void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
        updateDuration(subTask, true);
        updateStartTime(subTask, true);
        updatedEndTime(subTask, true);
    }

    private void updateStartTime(SubTask subTask, boolean addTask) {
        if (addTask) {
            if (!(getStartTime().equals(LocalDateTime.parse("2100-01-01T00:00")))) {
                setStartTime(
                        getStartTime().isBefore(subTask.getStartTime()) ? getStartTime() : subTask.getStartTime()
                );
            } else {
                setStartTime(subTask.getStartTime());
            }
        } else {
            Optional<SubTask> task = subTasks
                    .stream()
                    .min(Comparator.comparing(Task::getStartTime));
            if (task.isPresent()) {
                setStartTime(task.get().getStartTime());
            } else {
                setStartTime(LocalDateTime.parse("2100-01-01T00:00"));
            }
        }
    }

    private void updateDuration(SubTask subTask, boolean addTask) {
        if (addTask) {
            setDuration(getDuration().plus(subTask.getDuration()));
        } else {
            setDuration(getDuration().minus(subTask.getDuration()));
        }
    }

    private void updatedEndTime(SubTask subTask, boolean addTask) {
        if (addTask) {
            if (getEndTime() != null) {
                setEndTime(getEndTime().isBefore(subTask.getEndTime()) ? subTask.getEndTime() : getEndTime());
            } else {
                setEndTime(subTask.getEndTime());
            }
        } else {
            Optional<SubTask> task = subTasks
                    .stream()
                    .findFirst();

            if (task.isPresent()) {
                setEndTime(task.get().getEndTime());
            } else {
                setEndTime(null);
            }
        }
    }

    public void removeSubTask(SubTask subTask) {
        subTasks.remove(subTask);

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
