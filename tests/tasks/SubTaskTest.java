package tasks;

import main.java.tasks.Status;
import main.java.tasks.SubTask;
import main.java.tasks.Type;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubTaskTest {

    @Test
    public void test1_shouldReturnTaskType() {
        SubTask subTask = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        Type expectedType = Type.SUB;

        assertEquals(expectedType, subTask.getType());
    }

    public
    @Test void test2_shouldReturnTaskId() {
        SubTask subTask = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        int expected = 1;

        assertEquals(expected, subTask.getId());
    }

    @Test
    public void test3_shouldReturnTaskName() {
        SubTask subTask = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        String expected = "SubTask #1";

        assertEquals(expected, subTask.getName());
    }

    @Test
    public void test4_shouldReturnTaskDescription() {
        SubTask subTask = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        String expected = "Simple SubTask";

        assertEquals(expected, subTask.getDescription());
    }

    @Test
    public void test5_shouldReturnTaskStatus() {
        SubTask subTask = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        Status expected = Status.NEW;

        assertEquals(expected, subTask.getStatus());
    }

    @Test
    public void test6_shouldReturnEpicId() {
        SubTask subTask = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        int expected = 0;

        assertEquals(expected, subTask.getEpicId());
    }

    @Test
    public void test7_shouldReturnEquals() {
        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        Assertions.assertEquals(subTask1, subTask2);
    }

    @Test
    public void test8_shouldReturnNotEquals() {
        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        Assertions.assertNotEquals(subTask1, subTask2);
    }

    @Test
    public void test9_shouldReturnEqualsHashCode() {
        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        assertEquals(subTask1.hashCode(), subTask2.hashCode());
    }

    @Test
    public void test10_shouldReturnDifferentHashCode() {
        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        Assertions.assertNotEquals(subTask1.hashCode(), subTask2.hashCode());
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

        assertEquals(expected, subTask.toString());
    }
}
