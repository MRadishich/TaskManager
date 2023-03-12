package dto;

import main.java.dto.TaskDTO;
import main.java.tasks.Epic;
import main.java.tasks.Status;
import main.java.tasks.Type;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TaskDTOTest {

    @Test
    public void test1_shouldReturnTaskDTOFromString() {
        TaskDTO epic1 = TaskDTO.getTaskDTO("0,EPIC,Epic #1,Simple Epic,NEW,540,2023-03-03T10:00,null", ",");

        assertEquals(0, epic1.getId(), "id не совпадает.");
        assertEquals(Type.EPIC, epic1.getType(), "тип не совпадает.");
        assertEquals("Epic #1", epic1.getName(), "Наименование не совпадает.");
        assertEquals("Simple Epic", epic1.getDescription(), "Описание не совпадает.");
        assertEquals(Status.NEW, epic1.getStatus(), "Статус не совпадает.");
        assertEquals(Duration.ofMinutes(540), epic1.getDuration());
        assertEquals(LocalDateTime.parse("2023-03-03T10:00"), epic1.getStartTime());
        assertNull(epic1.getEpicId());
    }

    @Test
    public void test2_shouldReturnTaskDTOFromStringIfStatusDurationStartTimeIsNull() {
        TaskDTO epic1 = TaskDTO.getTaskDTO("0,EPIC,Epic #1,Simple Epic,null,null,null,null", ",");

        assertEquals(0, epic1.getId(), "id не совпадает.");
        assertEquals(Type.EPIC, epic1.getType(), "тип не совпадает.");
        assertEquals("Epic #1", epic1.getName(), "Наименование не совпадает.");
        assertEquals("Simple Epic", epic1.getDescription(), "Описание не совпадает.");
        assertEquals(Status.NEW, epic1.getStatus(), "Статус не совпадает.");
        assertEquals(Duration.ofMinutes(0), epic1.getDuration());
        assertEquals(LocalDateTime.parse("2100-01-01T00:00"), epic1.getStartTime());
        assertNull(epic1.getEpicId());
    }

    @Test
    public void test3_shouldWrappingTaskToTaskDTO() {
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
    public void test4_shouldReturnTaskDTOasString() {
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
