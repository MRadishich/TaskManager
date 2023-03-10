package tasks;

import main.java.tasks.Status;
import main.java.tasks.SubTask;
import main.java.tasks.Type;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class SubTaskTest {

    @Test
    public void test1_shouldReturnTaskType() {
        SubTask subTask = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        assertEquals(Type.SUB, subTask.getType(), "Тип отличается.");
    }

    public
    @Test void test2_shouldReturnTaskId() {
        SubTask subTask = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        assertEquals(1, subTask.getId(), "Id отличается.");
    }

    @Test
    public void test3_shouldReturnTaskName() {
        SubTask subTask = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        String expected = "SubTask #1";

        assertEquals(expected, subTask.getName(), "Наименование подзадачи отличается.");
    }

    @Test
    public void test4_shouldReturnTaskDescription() {
        SubTask subTask = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        String expected = "Simple SubTask";

        assertEquals(expected, subTask.getDescription(), "Описание подзадачи отличается.");
    }

    @Test
    public void test5_shouldReturnTaskStatus() {
        SubTask subTask = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        assertEquals(Status.NEW, subTask.getStatus(), "Статус подздачи отличается.");
    }

    @Test
    public void test6_shouldReturnEpicId() {
        SubTask subTask = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        assertEquals(0, subTask.getEpicId(), "Id эпика отличается.");
    }

    @Test
    public void test7_shouldReturnEquals() {
        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        assertEquals(subTask1, subTask2, "Подздачи отличаются.");
    }

    @Test
    public void test8_shouldReturnNotEquals() {
        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        assertNotEquals(subTask1, subTask2, "Подзадачи совпадают.");
    }

    @Test
    public void test9_shouldReturnEqualsHashCode() {
        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        assertEquals(subTask1.hashCode(), subTask2.hashCode(), "HashCode отличаестся.");
    }

    @Test
    public void test10_shouldReturnDifferentHashCode() {
        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        assertNotEquals(subTask1.hashCode(), subTask2.hashCode(), "HashCode совпадает.");
    }

    @Test
    public void test11_shouldReturnReadableToStringResult() {
        SubTask subTask = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        String expected = "SubTask{id='1', " +
                "epicId='0', " +
                "name='SubTask #1', " +
                "descriptionLength='14', " +
                "status='NEW', duration='540', " +
                "startTime='2023-03-03T10:00', " +
                "endTime='2023-03-03T19:00'" +
                "}";

        assertEquals(expected, subTask.toString(), "toString() не совпадает.");
    }

    @Test
    public void test12_shouldReturnDuration() {
        SubTask subTask = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        assertEquals(540, subTask.getDuration().toMinutes(), "Дюрация отличается.");

        subTask = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, null, LocalDateTime.parse("2023-03-03T10:00"), 0);

        assertEquals(0, subTask.getDuration().toMinutes(), "Дюрация отличается.");
    }

    @Test
    public void test13_shouldReturnStartTime() {
        SubTask subTask = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        assertEquals(LocalDateTime.parse("2023-03-03T10:00"), subTask.getStartTime());

        subTask = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), null, 0);

        assertEquals(LocalDateTime.parse("2100-01-01T00:00"), subTask.getStartTime());
    }

    @Test
    public void test14_shouldReturnEndTime() {
        SubTask subTask = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        assertEquals(LocalDateTime.parse("2023-03-03T19:00"), subTask.getEndTime());

        subTask = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), null, 0);

        assertEquals(LocalDateTime.parse("2100-01-01T00:00"), subTask.getEndTime());

        subTask = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, null, null, 0);

        assertEquals(LocalDateTime.parse("2100-01-01T00:00"), subTask.getEndTime());
    }
}
