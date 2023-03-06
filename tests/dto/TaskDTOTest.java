package dto;

import main.java.dto.TaskDTO;
import main.java.tasks.Epic;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

public class TaskDTOTest {
    @Test
    public void taskToTaskDTO() {
        Epic epic = new Epic(
                0,
                "Epic #1",
                "Simple Epic",
                Duration.ofMinutes(540L),
                LocalDateTime.parse("2023-03-03T10:00")
        );


        System.out.println(TaskDTO.toTaskDTO(epic).asString());
    }
}
