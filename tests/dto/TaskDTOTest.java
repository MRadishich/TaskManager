package dto;

import main.java.dto.TaskDTO;
import main.java.tasks.Epic;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskDTOTest {

    @Test
    public void createTaskDTOFromString() {
        TaskDTO.getTaskDTO("0,EPIC,Epic #1,Simple Epic,NEW,540,2023-03-03T10:00,null", ",");

    }

    @Test
    public void wrappingTaskToTaskDTO() {
        Epic epic = new Epic(
                0,
                "Epic #1",
                "Simple Epic",
                Duration.ofMinutes(540L),
                LocalDateTime.parse("2023-03-03T10:00")
        );

        TaskDTO epicToDTO = TaskDTO.toTaskDTO(epic);
        TaskDTO taskDTO = TaskDTO.getTaskDTO("0,EPIC,Epic #1,Simple Epic,NEW,540,2023-03-03T10:00,null", ",");

        assertEquals(taskDTO, epicToDTO);
    }

    @Test
    public void shouldReturnTaskDTOasString() {
        Epic epic = new Epic(
                0,
                "Epic #1",
                "Simple Epic",
                Duration.ofMinutes(540L),
                LocalDateTime.parse("2023-03-03T10:00")
        );

        String taskDTO = TaskDTO.toTaskDTO(epic).asString();

        assertEquals("0,EPIC,Epic #1,Simple Epic,NEW,540,2023-03-03T10:00,null", taskDTO);
    }
}
