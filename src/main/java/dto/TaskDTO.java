package main.java.dto;

import main.java.tasks.Status;
import main.java.tasks.SubTask;
import main.java.tasks.Task;
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

    private TaskDTO(
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

    public static TaskDTO getTaskDTO(String line, String separator) {
        String[] data = line.split(separator);

        return new TaskDTO(
                "null".equals(data[0]) ? null : Integer.parseInt(data[0]),
                Type.valueOfOrTrow(data[1]),
                data[2],
                data[3],
                "null".equals(data[4]) ? Status.NEW : Status.valueOfOrTrow(data[4]),
                "null".equals(data[5]) ? Duration.ofMinutes(0) : Duration.ofMinutes(Integer.parseInt(data[5])),
                "null".equals(data[6]) ? LocalDateTime.parse("2100-01-01T00:00") : LocalDateTime.parse(data[6]),
                data.length > 7 && !"null".equals(data[7]) ? Integer.parseInt(data[7]) : null
        );
    }

    public static TaskDTO toTaskDTO(Task task) {
        switch (task.getType()) {
            case SINGLE:
            case EPIC:
                return new TaskDTO(
                        task.getId(),
                        task.getType(),
                        task.getName(),
                        task.getDescription(),
                        task.getStatus(),
                        task.getDuration(),
                        task.getStartTime(),
                        null
                );
            case SUB:
                return new TaskDTO(
                        task.getId(),
                        task.getType(),
                        task.getName(),
                        task.getDescription(),
                        task.getStatus(),
                        task.getDuration(),
                        task.getStartTime(),
                        ((SubTask) task).getEpicId()
                );
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: '" + task.getType() + "'");
        }
    }

    public String asString() {
        return String.format(
                "%s,%s,%s,%s,%s,%s,%s,%s",
                id,
                type,
                name,
                description,
                status,
                duration.toMinutes(),
                startTime,
                epicId
        );
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
