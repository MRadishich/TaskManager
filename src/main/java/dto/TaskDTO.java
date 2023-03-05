package main.java.dto;

import main.java.tasks.Status;
import main.java.tasks.Type;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class TaskDTO {
    private final Integer id;
    private final Type type;
    private final String name;
    private final String description;
    private final Status status;
    private final Integer epicId;
    private final Duration duration;
    private final LocalDateTime startTime;

    public TaskDTO(
            Integer id,
            Type type,
            String name,
            String description,
            Status status,
            Duration duration,
            LocalDateTime startTime,
            Integer epicId
    ) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
        this.epicId = epicId;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Type getType() {
        return type;
    }

    public Status getStatus() {
        return status;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
    public int getEpicId() {
        return epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskDTO taskDTO = (TaskDTO) o;
        return id.equals(taskDTO.id) && type == taskDTO.type && name.equals(taskDTO.name) && description.equals(taskDTO.description) && status == taskDTO.status && Objects.equals(epicId, taskDTO.epicId) && Objects.equals(duration, taskDTO.duration) && Objects.equals(startTime, taskDTO.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, name, description, status, epicId, duration, startTime);
    }

    @Override
    public String toString() {
        return "TaskDTO{" +
                "id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", epicId=" + epicId +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }
}
