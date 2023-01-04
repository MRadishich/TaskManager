package main.java.tasks;

import java.util.Objects;

public class SingleTask extends Task {
    private final Status status;

    public SingleTask(int id, String name, String description, Status status) {
        super(id, name, description);
        this.status = status;
    }

    @Override
    public Type getType() {
        return Type.SINGLE;
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
        SingleTask epic = (SingleTask) o;
        return status == epic.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), status);
    }

    @Override
    public String toString() {
        return "SingleTask{" +
                "Id='" + getId() + '\'' +
                ", Type='" + getType() + '\'' +
                ", Name='" + getName() + '\'' +
                ", DescriptionLength='" + getDescription().length() + '\'' +
                ", Status='" + status + '\'' +
                '}';
    }
}
