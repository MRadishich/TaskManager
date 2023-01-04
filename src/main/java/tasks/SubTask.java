package main.java.tasks;

import java.util.Objects;

public class SubTask extends Task {
    private final Status status;
    private final int epicId;

    public SubTask(int id, String name, String description, Status status, int epicId) {
        super(id, name, description);
        this.status = status;
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public Type getType() {
        return Type.SUB;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return epicId == subTask.epicId && status == subTask.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), status, epicId);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "Id='" + getId() + '\'' +
                ", Type='" + getType() + '\'' +
                ", EpicId='" + getEpicId() + '\'' +
                ", Name='" + getName() + '\'' +
                ", DescriptionLength='" + getDescription().length() + '\'' +
                ", Status='" + status + '\'' +
                '}';
    }
}
