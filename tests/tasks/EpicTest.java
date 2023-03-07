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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EpicTest {
    @Test
    public void test1_shouldReturnTaskType() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        Type expected = Type.EPIC;

        assertEquals(expected, epic.getType());
    }

    @Test
    public void test2_shouldReturnTaskId() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        int expected = 0;

        assertEquals(expected, epic.getId());
    }

    @Test
    public void test3_shouldReturnTaskName() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        String expected = "Epic #1";

        assertEquals(expected, epic.getName());
    }

    @Test
    public void test4_shouldReturnTaskDescription() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        String expected = "Simple Epic";

        assertEquals(expected, epic.getDescription());
    }

    @Test
    public void test5_shouldReturnTaskStatus() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        Status expected = Status.NEW;

        assertEquals(expected, epic.getStatus());
    }

    @Test
    public void test6_shouldReturnEmptySubTaskList() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        assertTrue(epic.getSubTasks().isEmpty());
    }

    @Test
    public void test7_shouldReturnSubTaskList() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        epic.addSubTask(subTask1.getId(), subTask1);
        epic.addSubTask(subTask2.getId(), subTask2);

        List<SubTask> expected = List.of(subTask1, subTask2);

        assertEquals(expected, epic.getSubTasks());
    }

    @Test
    public void test8_shouldReturnTaskStatusNewIfAllSubTaskHaveStatusNew() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        epic.addSubTask(subTask1.getId(), subTask1);
        epic.addSubTask(subTask2.getId(), subTask2);

        Status expected = Status.NEW;

        assertEquals(expected, epic.getStatus());
    }

    @Test
    public void test9_shouldReturnTaskStatusInProgressIfSubTaskHaveDifferentStatus() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        epic.addSubTask(subTask1.getId(), subTask1);
        epic.addSubTask(subTask2.getId(), subTask2);

        Status expected = Status.IN_PROGRESS;

        assertEquals(expected, epic.getStatus());
    }

    @Test
    public void test10_shouldReturnTaskStatusInProgressIfAllSubTaskHaveStatusInProgress() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.IN_PROGRESS, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.IN_PROGRESS, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        epic.addSubTask(subTask1.getId(), subTask1);
        epic.addSubTask(subTask2.getId(), subTask2);

        Status expected = Status.IN_PROGRESS;

        assertEquals(expected, epic.getStatus());
    }

    @Test
    public void test11_shouldReturnTaskStatusDoneIfAllSubTaskHaveStatusDone() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        epic.addSubTask(subTask1.getId(), subTask1);
        epic.addSubTask(subTask2.getId(), subTask2);

        Status expected = Status.DONE;

        assertEquals(expected, epic.getStatus());
    }

    @Test
    public void test12_shouldReturnEquals() {
        Epic epic1 = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        Epic epic2 = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        epic1.addSubTask(subTask1.getId(), subTask1);
        epic1.addSubTask(subTask2.getId(), subTask2);

        epic2.addSubTask(subTask1.getId(), subTask1);
        epic2.addSubTask(subTask2.getId(), subTask2);

        assertEquals(epic1, epic2);
    }

    @Test
    public void test12_shouldReturnNotEquals() {
        Epic epic1 = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        Epic epic2 = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

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
        Epic epic1 = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        Epic epic2 = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        epic1.addSubTask(subTask1.getId(), subTask1);
        epic1.addSubTask(subTask2.getId(), subTask2);

        epic2.addSubTask(subTask1.getId(), subTask1);
        epic2.addSubTask(subTask2.getId(), subTask2);

        assertEquals(epic1.hashCode(), epic2.hashCode());
    }

    @Test
    public void test15_shouldReturnDifferentHashCode() {
        Epic epic1 = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        Epic epic2 = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

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
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        String expected = "Epic{Id='0', Name='Epic #1', DescriptionLength='11', Status='NEW', NumberOfSubTask: '0'}";

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
}
