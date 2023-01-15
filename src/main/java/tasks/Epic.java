package main.java.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private final HashMap<Integer, SubTask> subTasks;

    public Epic(int id, String name, String description) {
        super(id, name, description, Status.NEW);
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

    private boolean areAllSubTasksCompleted() {
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getStatus() != Status.DONE) {
                return false;
            }
        }

        return true;
    }

    private boolean areAllSubTaskNew() {
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getStatus() != Status.NEW) {
                return false;
            }
        }

        return true;
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
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTasks, epic.subTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasks);
    }
}