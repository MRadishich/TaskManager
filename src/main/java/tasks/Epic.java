package main.java.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private final HashMap<Integer, SubTask> subTasks;

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

    public void addSubTask(int id, SubTask subTask) {
        subTasks.put(id, subTask);
    }

    public void removeSubTask(int id) {
        subTasks.remove(id);
    }

    public void removeAllSubTask() {
        subTasks.clear();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "Id='" + getId() + '\'' +
                ", Name='" + getName() + '\'' +
                ", DescriptionLength='" + getDescription().length() + '\'' +
                ", Status='" + getStatus() +
                "'}";
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
}
