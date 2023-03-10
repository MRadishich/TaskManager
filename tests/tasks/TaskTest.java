package tasks;

import main.java.tasks.Status;
import main.java.tasks.Task;
import main.java.tasks.Type;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {
    @Test
    public void test1_shouldReturnTaskType() {
        Task task = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        assertEquals(Type.SINGLE, task.getType(), "Тип отличаются.");
    }

    @Test
    public void test2_shouldReturnTaskId() {
        Task task = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        assertEquals(0, task.getId(), "Id отличаются.");
    }

    @Test
    public void test3_shouldReturnTaskName() {
        Task task = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        String expected = "Single Task #1";

        assertEquals(expected, task.getName(), "Наименование задачи отличается.");
    }

    @Test
    public void test4_shouldReturnTaskDescription() {
        Task task = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        String expected = "Simple single task";

        assertEquals(expected, task.getDescription(), "Описание задачи отличается.");
    }

    @Test
    public void test5_shouldReturnTaskStatus() {
        Task task = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        assertEquals(Status.NEW, task.getStatus(), "Статус задачи отличается.");
    }

    @Test
    public void test6_shouldReturnEquals() {
        Task task1 = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));
        Task task2 = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        assertEquals(task1, task2, "Задачи не совпадают.");
    }

    @Test
    public void test7_shouldReturnNotEquals() {
        Task task1 = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));
        Task task2 = new Task(0, "Single Task #1", "Simple single task", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        assertNotEquals(task1, task2, "Задачи совпадают.");
    }

    @Test
    public void test8_shouldReturnEqualsHashCode() {
        Task task1 = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));
        Task task2 = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        assertEquals(task1.hashCode(), task2.hashCode(), "HashCode не совпадает.");
    }

    @Test
    public void test9_shouldReturnDifferentHashCode() {
        Task task1 = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));
        Task task2 = new Task(0, "Single Task #1", "Simple single task", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        assertNotEquals(task1.hashCode(), task2.hashCode(), "HashCode совпадает.");
    }

    @Test
    public void test10_shouldReturnReadableToStringResult() {
        Task task = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        String expected = "SingleTask{id='0', " +
                "name='Single Task #1', " +
                "descriptionLength='18', " +
                "status='NEW', " +
                "duration='540', " +
                "startTime='2023-03-03T10:00', " +
                "endTime='2023-03-03T19:00" +
                "'}";

        assertEquals(expected, task.toString(), "toString() не совпадает.");
    }

    @Test
    public void test11_shouldReturnTaskDuration() {
        Task task1 = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        assertEquals(540, task1.getDuration().toMinutes());

        Task task2 = new Task(0, "Single Task #1", "Simple single task", Status.NEW, null, LocalDateTime.parse("2023-03-03T10:00"));

        assertEquals(0, task2.getDuration().toMinutes(), "Дюрация не совпадает.");
    }

    @Test
    public void test12_shouldReturnStartTime() {
        Task task1 = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        assertEquals("2023-03-03T10:00", task1.getStartTime().toString(), "Дата начала задачи не совпадает.");

        Task task2 = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), null);

        assertEquals(LocalDateTime.parse("2100-01-01T00:00"), task2.getStartTime(), "Дата начала задачи не совпадает.");
    }

    @Test
    public void test13_shouldReturnTaskEndTime() {
        Task task1 = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        assertEquals(LocalDateTime.parse("2023-03-03T19:00"), task1.getEndTime(), "Дата окончания задачи не совпадает.");

        Task task2 = new Task(1, "Single Task #1", "Simple single task", Status.NEW, null, null);

        assertEquals(LocalDateTime.parse("2100-01-01T00:00"), task2.getEndTime(), "Дата окончания задачи не совпадает.");
    }

    @Test
    public void test14_shouldReturnZeroDurationIsNotGiven() {
        Task task = new Task(0, "Single Task #1", "Simple single task", Status.NEW, null, LocalDateTime.parse("2023-03-03T10:00"));

        assertTrue(task.getDuration().isZero(), "Дюрация не равна 0.");
    }
}
