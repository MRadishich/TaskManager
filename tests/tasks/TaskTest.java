package tasks;

import main.java.tasks.Status;
import main.java.tasks.Task;
import main.java.tasks.Type;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TaskTest {
    @Test
    public void test1_shouldReturnTaskType() {
        Task task = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        Type expected = Type.SINGLE;

        assertEquals(expected, task.getType());
    }

    @Test
    public void test2_shouldReturnTaskId() {
        Task task = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        int expected = 0;

        assertEquals(expected, task.getId());
    }

    @Test
    public void test3_shouldReturnTaskName() {
        Task task = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        String expected = "Single Task #1";

        assertEquals(expected, task.getName());
    }

    @Test
    public void test4_shouldReturnTaskDescription() {
        Task task = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        String expected = "Simple single task";

        assertEquals(expected, task.getDescription());
    }

    @Test
    public void test5_shouldReturnTaskStatus() {
        Task task = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        Status expected = Status.NEW;

        assertEquals(expected, task.getStatus());
    }

    @Test
    public void test6_shouldReturnTaskStatusNewIfTaskCreatedWithoutStatus() {
        Task task = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        Status expected = Status.NEW;

        assertEquals(expected, task.getStatus());
    }

    @Test
    public void test7_shouldReturnEquals() {
        Task task1 = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));
        Task task2 = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        assertEquals(task1, task2);
    }

    @Test
    public void test8_shouldReturnNotEquals() {
        Task task1 = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));
        Task task2 = new Task(0, "Single Task #1", "Simple single task", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        Assertions.assertNotEquals(task1, task2);
    }

    @Test
    public void test9_shouldReturnEqualsHashCode() {
        Task task1 = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));
        Task task2 = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        assertEquals(task1.hashCode(), task2.hashCode());
    }

    @Test
    public void test10_shouldReturnDifferentHashCode() {
        Task task1 = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));
        Task task2 = new Task(0, "Single Task #1", "Simple single task", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        Assertions.assertNotEquals(task1.hashCode(), task2.hashCode());
    }

    @Test
    public void test11_shouldReturnReadableToStringResult() {
        Task task = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        String expected = "SingleTask{id='0', " +
                "name='Single Task #1', " +
                "descriptionLength='18', " +
                "status='NEW', " +
                "duration='540', " +
                "startTime='2023-03-03T10:00', " +
                "endTime='2023-03-03T19:00" +
                "'}";

        assertEquals(expected, task.toString());
    }

    @Test
    public void test12_shouldReturnTaskDuration() {
        Task task1 = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        assertEquals(540, task1.getDuration().toMinutes());

        Task task2 = new Task(0, "Single Task #1", "Simple single task", Status.NEW, null, LocalDateTime.parse("2023-03-03T10:00"));

        assertNull(task2.getDuration());
    }

    @Test
    public void test13_shouldReturnStartTime() {
        Task task1 = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        assertEquals("2023-03-03T10:00", task1.getStartTime().toString());

        Task task2 = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), null);

        assertNull(task2.getStartTime());
    }

    @Test
    public void test13_shouldReturnTaskEndTime() {
        Task task1 = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        assertEquals("2023-03-03T19:00", task1.getEndTime().toString());

        Task task2 = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), null);

        assertNull(task2.getEndTime());
    }

    @Test
    public void test14_shouldReturnNullIfDurationIsNotGiven() {
        Task task = new Task(0, "Single Task #1", "Simple single task", Status.NEW, null, LocalDateTime.parse("2023-03-03T10:00"));

        assertNull(task.getDuration());
    }
}
