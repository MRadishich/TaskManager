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
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", null, null);

        assertEquals(Type.EPIC, epic.getType(), "Тип отличается.");
    }

    @Test
    public void test2_shouldReturnTaskId() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", null, null);

        assertEquals(0, epic.getId(), "id отличается.");
    }

    @Test
    public void test3_shouldReturnTaskName() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", null, null);

        assertEquals("Epic #1", epic.getName(), "Наименование Эпика отличается.");
    }

    @Test
    public void test4_shouldReturnTaskDescription() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", null, null);


        assertEquals("Simple Epic", epic.getDescription(), "Описание отличается.");
    }

    @Test
    public void test5_shouldReturnTaskStatus() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", null, null);

        assertEquals(Status.NEW, epic.getStatus(), "Статус отличается");
    }

    @Test
    public void test6_shouldReturnEmptySubTaskList() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", null, null);

        assertTrue(epic.getSubTasks().isEmpty(), "Список подзадач не пустой.");
    }

    @Test
    public void test7_shouldReturnSubTaskList() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", null, null);

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T11:00"), 0);

        epic.addSubTask(subTask1);
        epic.addSubTask(subTask2);

        List<SubTask> expected = List.of(subTask1, subTask2);

        assertEquals(expected, epic.getSubTasks(), "Список подзадач отличается.");
    }

    @Test
    public void test8_shouldReturnTaskStatusNewIfAllSubTaskHaveStatusNew() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", null, null);

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T11:00"), 0);

        epic.addSubTask(subTask1);
        epic.addSubTask(subTask2);

        assertEquals(Status.NEW, epic.getStatus(), "Статус отличается.");
    }

    @Test
    public void test9_shouldReturnTaskStatusInProgressIfSubTaskHaveDifferentStatus() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", null, null);

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T11:00"), 0);

        epic.addSubTask(subTask1);
        epic.addSubTask(subTask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус отличается.");
    }

    @Test
    public void test10_shouldReturnTaskStatusInProgressIfAllSubTaskHaveStatusInProgress() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", null, null);

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.IN_PROGRESS, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.IN_PROGRESS, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        epic.addSubTask(subTask1);
        epic.addSubTask(subTask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус отличается.");
    }

    @Test
    public void test11_shouldReturnTaskStatusDoneIfAllSubTaskHaveStatusDone() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", null, null);

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        epic.addSubTask(subTask1);
        epic.addSubTask(subTask2);

        assertEquals(Status.DONE, epic.getStatus(), "Статус отличается.");
    }

    @Test
    public void test12_shouldReturnEquals() {
        Epic epic1 = new Epic(0, "Epic #1", "Simple Epic", null, null);

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        Epic epic2 = new Epic(0, "Epic #1", "Simple Epic", null, null);

        epic1.addSubTask(subTask1);
        epic1.addSubTask(subTask2);

        epic2.addSubTask(subTask1);
        epic2.addSubTask(subTask2);

        assertEquals(epic1, epic2, "Эпики не совпадают.");
    }

    @Test
    public void test12_shouldReturnNotEquals() {
        Epic epic1 = new Epic(0, "Epic #1", "Simple Epic", null, null);

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        Epic epic2 = new Epic(0, "Epic #1", "Simple Epic", null, null);

        epic1.addSubTask(subTask1);
        epic1.addSubTask(subTask2);

        SubTask subTask3 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask4 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T11:00"), 0);

        epic2.addSubTask(subTask3);
        epic2.addSubTask(subTask4);

        assertNotEquals(epic1, epic2, "Эпики совпадают.");
    }

    @Test
    public void test14_shouldReturnEqualsHashCode() {
        Epic epic1 = new Epic(0, "Epic #1", "Simple Epic", null, null);

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        Epic epic2 = new Epic(0, "Epic #1", "Simple Epic", null, null);

        epic1.addSubTask(subTask1);
        epic1.addSubTask(subTask2);

        epic2.addSubTask(subTask1);
        epic2.addSubTask(subTask2);

        assertEquals(epic1.hashCode(), epic2.hashCode(), "HashCode не совпадатае.");
    }

    @Test
    public void test15_shouldReturnDifferentHashCode() {
        Epic epic1 = new Epic(0, "Epic #1", "Simple Epic", null, null);

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        Epic epic2 = new Epic(0, "Epic #1", "Simple Epic", null, null);

        epic1.addSubTask(subTask1);
        epic1.addSubTask(subTask2);

        SubTask subTask3 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask4 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        epic2.addSubTask(subTask3);
        epic2.addSubTask(subTask4);

        assertNotEquals(epic1.hashCode(), epic2.hashCode(), "HashCode совпадает.");
    }

    @Test
    public void test16_shouldReturnReadableToStringResult() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", null, null);

        String expected = "Epic{id='0', name='Epic #1', descriptionLength='11', status='NEW', numberOfSubTask='0', duration='0', startTime='2100-01-01T00:00', endTime='null'}";

        assertEquals(expected, epic.toString(), "toString() не совпадает.");
    }

    @Test
    public void test18_shouldReturnTaskListWithoutRemovingSubTask() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T11:00"), 0);

        epic.addSubTask(subTask1);
        epic.addSubTask(subTask2);

        epic.removeSubTask(subTask1);

        assertEquals(List.of(subTask2), epic.getSubTasks(), "Список не совпадает.");
    }

    @Test
    public void test19_shouldReturnEmptyTaskListAfterRemovingAllSubTasks() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        epic.addSubTask(subTask1);
        epic.addSubTask(subTask2);

        epic.removeAllSubTasks();

        assertEquals(List.of(), epic.getSubTasks(), "Список подзадач не пустой.");
    }

    @Test
    public void test20_shouldReturnSubTaskSortedByStartTime() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", null, null);

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-05T10:00"), 0);
        SubTask subTask2 = new SubTask(2, "SubTask #2", "Simple SubTask", Status.IN_PROGRESS, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask3 = new SubTask(3, "SubTask #3", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-01T10:00"), 0);
        SubTask subTask4 = new SubTask(4, "SubTask #4", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-02T09:00"), 0);
        SubTask subTask5 = new SubTask(4, "SubTask #4", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), null, 0);

        epic.addSubTask(subTask1);
        epic.addSubTask(subTask2);
        epic.addSubTask(subTask3);
        epic.addSubTask(subTask4);
        epic.addSubTask(subTask5);

        assertEquals(subTask1, epic.getSubTasks().get(3), "Подзадачи не совпадают.");
        assertEquals(subTask2, epic.getSubTasks().get(2), "Подзадачи не совпадают.");
        assertEquals(subTask3, epic.getSubTasks().get(0), "Подзадачи не совпадают.");
        assertEquals(subTask4, epic.getSubTasks().get(1), "Подзадачи не совпадают.");
        assertEquals(subTask4, epic.getSubTasks().get(4), "Подзадачи не совпадают.");
    }

    @Test
    public void test21_shouldReturnSumDurationSubTasks() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", null, null);

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-04-01T10:00"), 0);
        SubTask subTask2 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-05-02T10:00"), 0);
        SubTask subTask3 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-28T10:00"), 0);
        SubTask subTask4 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-04-05T10:00"), 0);

        epic.addSubTask(subTask1);
        epic.addSubTask(subTask2);
        epic.addSubTask(subTask3);
        epic.addSubTask(subTask4);

        assertEquals(2160, epic.getDuration().toMinutes(), "Дюрация не совпадает.");

        epic.removeSubTask(subTask4);

        assertEquals(1620, epic.getDuration().toMinutes(), "Дюрация не совпадает.");

        epic.removeSubTask(subTask1);
        epic.removeSubTask(subTask2);
        epic.removeSubTask(subTask3);

        assertEquals(0, epic.getDuration().toMinutes(), "Дюрация не совпадает.");
    }

    @Test
    public void test22_shouldReturnFirstStartTimeSubTaskAndLastEndTimeSubTask() {
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", null, null);

        SubTask subTask1 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-04-01T10:00"), 0);
        SubTask subTask2 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-05-02T10:00"), 0);
        SubTask subTask3 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-28T10:00"), 0);
        SubTask subTask4 = new SubTask(1, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-04-05T10:00"), 0);

        epic.addSubTask(subTask1);
        epic.addSubTask(subTask2);
        epic.addSubTask(subTask3);
        epic.addSubTask(subTask4);

        assertEquals(subTask3.getStartTime(), epic.getStartTime(), "Начало эпика отличается от время начала первой задачи.");
        assertEquals(subTask2.getEndTime(), epic.getEndTime(), "Окончание эпика отличается от времени окончания последней задачи.");
    }
}
