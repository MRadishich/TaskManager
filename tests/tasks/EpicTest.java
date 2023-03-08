package tasks;

import main.java.tasks.Epic;
import main.java.tasks.Status;
import main.java.tasks.SubTask;
import main.java.tasks.Type;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {
    @Test
    public void test1_shouldReturnTaskType() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(0L), null);

        Type expected = Type.EPIC;

        assertEquals(expected, epic.getType());
    }

    @Test
    public void test2_shouldReturnTaskId() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(0L), null);

        assertEquals(0, epic.getId());
    }

    @Test
    public void test3_shouldReturnTaskName() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(0L), null);

        assertEquals("Epic #1", epic.getName());
    }

    @Test
    public void test4_shouldReturnTaskDescription() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(0L), null);


        assertEquals("Simple Epic", epic.getDescription());
    }

    @Test
    public void test5_shouldReturnTaskStatus() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(0L), null);

        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void test6_shouldReturnEmptySubTaskList() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(0L), null);

        assertTrue(epic.getSubTasks().isEmpty());
    }

    @Test
    public void test7_shouldReturnSubTaskList() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(0L), null);

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        epic.addSubTask(subTask1.getId(), subTask1);
        epic.addSubTask(subTask2.getId(), subTask2);

        List<SubTask> expected = List.of(subTask1, subTask2);

        assertEquals(expected, epic.getSubTasks());
    }

    @Test
    public void test8_shouldReturnTaskStatusNewIfAllSubTaskHaveStatusNew() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(0L), null);

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        epic.addSubTask(subTask1.getId(), subTask1);
        epic.addSubTask(subTask2.getId(), subTask2);

        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void test9_shouldReturnTaskStatusInProgressIfSubTaskHaveDifferentStatus() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(0L), null);

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        epic.addSubTask(subTask1.getId(), subTask1);
        epic.addSubTask(subTask2.getId(), subTask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void test10_shouldReturnTaskStatusInProgressIfAllSubTaskHaveStatusInProgress() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(0L), null);

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.IN_PROGRESS, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.IN_PROGRESS, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        epic.addSubTask(subTask1.getId(), subTask1);
        epic.addSubTask(subTask2.getId(), subTask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void test11_shouldReturnTaskStatusDoneIfAllSubTaskHaveStatusDone() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(0L), null);

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        epic.addSubTask(subTask1.getId(), subTask1);
        epic.addSubTask(subTask2.getId(), subTask2);

        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    public void test12_shouldReturnEquals() {
        Epic epic1 = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(0L), null);

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        Epic epic2 = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(0L), null);

        epic1.addSubTask(subTask1.getId(), subTask1);
        epic1.addSubTask(subTask2.getId(), subTask2);

        epic2.addSubTask(subTask1.getId(), subTask1);
        epic2.addSubTask(subTask2.getId(), subTask2);

        assertEquals(epic1, epic2);
    }

    @Test
    public void test12_shouldReturnNotEquals() {
        Epic epic1 = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(0L), null);

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        Epic epic2 = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(0L), null);

        epic1.addSubTask(subTask1.getId(), subTask1);
        epic1.addSubTask(subTask2.getId(), subTask2);

        SubTask subTask3 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask4 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        epic2.addSubTask(subTask1.getId(), subTask3);
        epic2.addSubTask(subTask2.getId(), subTask4);

        Assertions.assertNotEquals(epic1, epic2);
    }

    @Test
    public void test14_shouldReturnEqualsHashCode() {
        Epic epic1 = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(0L), null);

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        Epic epic2 = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(0L), null);

        epic1.addSubTask(subTask1.getId(), subTask1);
        epic1.addSubTask(subTask2.getId(), subTask2);

        epic2.addSubTask(subTask1.getId(), subTask1);
        epic2.addSubTask(subTask2.getId(), subTask2);

        assertEquals(epic1.hashCode(), epic2.hashCode());
    }

    @Test
    public void test15_shouldReturnDifferentHashCode() {
        Epic epic1 = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(0L), null);

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        Epic epic2 = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(0L), null);

        epic1.addSubTask(subTask1.getId(), subTask1);
        epic1.addSubTask(subTask2.getId(), subTask2);

        SubTask subTask3 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask4 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        epic2.addSubTask(subTask1.getId(), subTask3);
        epic2.addSubTask(subTask2.getId(), subTask4);

        Assertions.assertNotEquals(epic1.hashCode(), epic2.hashCode());
    }

    @Test
    public void test16_shouldReturnReadableToStringResult() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(0L), null);

        String expected = "Epic{id='0', name='Epic #1', descriptionLength='11', status='NEW', numberOfSubTask='0', duration='0', startTime='null', endTime='null'}";

        assertEquals(expected, epic.toString());
    }

    @Test
    public void test18_shouldReturnTaskListWithoutRemovingSubTask() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        epic.addSubTask(subTask1.getId(), subTask1);
        epic.addSubTask(subTask2.getId(), subTask2);

        epic.removeSubTask(1);

        List<SubTask> expected = List.of(subTask2);

        assertEquals(expected, epic.getSubTasks());
    }

    @Test
    public void test19_shouldReturnEmptyTaskListAfterRemovingAllSubTasks() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        epic.addSubTask(subTask1.getId(), subTask1);
        epic.addSubTask(subTask2.getId(), subTask2);

        epic.removeAllSubTasks();

        List<SubTask> expected = List.of();

        assertEquals(expected, epic.getSubTasks());
    }

    @Test
    public void test20_shouldReturnSumDurationSubTasks() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(0L), null);

        assertEquals(0, epic.getDuration().toMinutes());
        assertNull(epic.getStartTime());
        assertNull(epic.getEndTime());

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-01T10:00"), 0);

        epic.addSubTask(subTask1.getId(),subTask1);
        epic.addSubTask(subTask2.getId(),subTask2);

        assertEquals(1080, epic.getDuration().toMinutes());
        assertEquals("2023-03-01T10:00", epic.getStartTime().toString());
        assertEquals("2023-03-03T19:00", epic.getEndTime().toString());

        epic.removeSubTask(1);

        assertEquals(540, epic.getDuration().toMinutes());
        assertEquals("2023-03-01T10:00", epic.getStartTime().toString());
        assertEquals("2023-03-01T19:00", epic.getEndTime().toString());

        SubTask subTask3 = new SubTask(3, "SubTask #3", "Simple SubTask", Status.DONE, Duration.ofMinutes(1440L), LocalDateTime.parse("2023-03-05T10:00"), 0);

        epic.addSubTask(subTask3.getId(), subTask3);
        epic.removeSubTask(2);

        assertEquals(1440, epic.getDuration().toMinutes());
        assertEquals("2023-03-05T10:00", epic.getStartTime().toString());
        assertEquals("2023-03-06T10:00", epic.getEndTime().toString());
    }
}
