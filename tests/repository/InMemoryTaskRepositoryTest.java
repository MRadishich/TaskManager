package repository;

import main.java.exceptions.EpicNotFoundException;
import main.java.exceptions.TaskNotFoundException;
import main.java.repository.InMemoryTaskRepository;
import main.java.repository.TaskRepository;
import main.java.tasks.*;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskRepositoryTest {

    public TaskRepository createTaskRepository() {
        var repository = new InMemoryTaskRepository();

        Epic epic1 = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));
        Epic epic2 = new Epic(1, "Epic #2", "Simple Epic", Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        repository.saveTask(epic1);
        repository.saveTask(epic2);

        SubTask subTask1 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"),0);
        SubTask subTask2 = new SubTask(3, "SubTask #2", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        repository.saveTask(subTask1);
        repository.saveTask(subTask2);

        Task singleTask1 = new Task(4, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));
        Task singleTask2 = new Task(5, "Single Task #2", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        repository.saveTask(singleTask1);
        repository.saveTask(singleTask2);

        return repository;
    }

    @Test
    public void test1_shouldReturnEmptyTaskList() {
        var repository = new InMemoryTaskRepository();
        var expected = List.of();

        assertEquals(expected, repository.getAllTasks());
    }

    @Test
    public void test2_shouldReturnListWithAllTask() {
        var repository = createTaskRepository();

        int expected = 6;

        assertEquals(expected, repository.getAllTasks().size());
    }


    @Test
    public void test3_shouldReturnAllEpicTasks() {
        var repository = createTaskRepository();
        int expected = 2;

        assertEquals(expected, repository.getAllEpic().size());
    }

    @Test
    public void test4_shouldReturnAllSubTasks() {
        var repository = createTaskRepository();
        int expected = 2;

        assertEquals(expected, repository.getAllSubTasks().size());
    }

    @Test
    public void test5_shouldReturnAllSingleTask() {
        var repository = createTaskRepository();
        int expected = 2;

        assertEquals(expected, repository.getAllSingleTasks().size());
    }

    @Test
    public void test6_shouldReturnTaskById() {
        var repository = createTaskRepository();

        final TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class,
                () -> repository.getTaskById(6)
        );

        assertEquals("Задача с id 6 не найдена.", exception.getMessage());

        SubTask subTask2 = new SubTask(3, "SubTask #2", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        assertEquals(subTask2, repository.getTaskById(3));

    }

    @Test
    public void test7_shouldReturnAllSubTaskByEpicId() {
        var repository = createTaskRepository();

        SubTask subTask1 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(3, "SubTask #2", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        List<Task> expected = List.of(subTask1, subTask2);

        assertEquals(expected, repository.getAllSubTasksByEpicId(0));

        final EpicNotFoundException exception = assertThrows(
                EpicNotFoundException.class,
                () -> repository.getAllSubTasksByEpicId(2)
        );

        assertEquals("Эпик с id 2 не найден.", exception.getMessage());
    }

    @Test
    public void test8_shouldReturnUpdatedTask() {
        var repository = createTaskRepository();

        var singleTask2 = new Task(5, "Updated Single Task #2", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        repository.updateTask(singleTask2);

        assertEquals(singleTask2, repository.getTaskById(5));

        int expectedNumberOfTask = 6;

        assertEquals(expectedNumberOfTask, repository.getAllTasks().size());

        final var newSingleTask = new Task(6, "Updated Single Task #2", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        final TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class,
                () -> repository.updateTask(newSingleTask)
        );

        assertEquals("Задача с id 6 не найдена.", exception.getMessage());
    }

    @Test
    public void test9_shouldReturnUpdatedEpic() {
        var repository = createTaskRepository();

        var epic1 = new Epic(0, "Updated Epic #1", "Simple Epic", Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        repository.updateTask(epic1);

        assertEquals(epic1, repository.getTaskById(0));

        int expectedNumberOfTask = 6;

        assertEquals(expectedNumberOfTask, repository.getAllTasks().size());
    }

    @Test
    public void test10_subTaskShouldBeUpdatedInEpicSubTaskList() {
        var repository = createTaskRepository();

        SubTask subTask1 = new SubTask(2, "SubTask #1", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        SubTask subTask2 = new SubTask(3, "SubTask #2", "Simple SubTask", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);

        List<SubTask> expectedListSubTaskFirstEpic = List.of(subTask1, subTask2);
        List<SubTask> expectedListSubTaskSecondEpic = List.of();

        assertEquals(expectedListSubTaskFirstEpic, repository.getAllSubTasksByEpicId(0));
        assertEquals(expectedListSubTaskSecondEpic, repository.getAllSubTasksByEpicId(1));

        subTask1 = new SubTask(2, "Updated SubTask #1", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 0);
        subTask2 = new SubTask(3, "Updated SubTask #2", "Simple SubTask", Status.DONE, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"), 1);

        repository.updateTask(subTask1);
        repository.updateTask(subTask2);

        assertEquals(subTask1, repository.getTaskById(2));
        assertEquals(subTask2, repository.getTaskById(3));

        expectedListSubTaskFirstEpic = List.of(subTask1);
        expectedListSubTaskSecondEpic = List.of(subTask2);

        assertEquals(expectedListSubTaskFirstEpic, repository.getAllSubTasksByEpicId(0));
        assertEquals(expectedListSubTaskSecondEpic, repository.getAllSubTasksByEpicId(1));

        int expectedNumberOfTask = 6;

        assertEquals(expectedNumberOfTask, repository.getAllTasks().size());
    }

    @Test
    public void test11_shouldReturnEmptyTaskListAfterRemoveAllTasks() {
        var repository = createTaskRepository();

        int expectedSizeListTaskBeforeRemove = 6;

        assertEquals(expectedSizeListTaskBeforeRemove, repository.getAllTasks().size());

        repository.removeAllTasks();

        assertTrue(repository.getAllTasks().isEmpty());
    }

    @Test
    public void test12_removeTaskById() {
        var repository = createTaskRepository();

        int expectedSizeListTaskBeforeRemove = 6;

        assertEquals(expectedSizeListTaskBeforeRemove, repository.getAllTasks().size());

        repository.removeTaskById(1);

        final TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class,
                () -> repository.removeTaskById(1)
        );

        assertEquals("Задача с id 1 не найдена.", exception.getMessage());

        int expectedSizeListSubTaskFirstEpicBeforeRemove = 2;

        assertEquals(expectedSizeListSubTaskFirstEpicBeforeRemove, repository.getAllSubTasksByEpicId(0).size());

        repository.removeTaskById(2);

        int expectedSizeListSubTaskFirstEpicAfterRemove = 1;

        assertEquals(expectedSizeListSubTaskFirstEpicAfterRemove, repository.getAllSubTasksByEpicId(0).size());

        repository.removeTaskById(0);

        assertEquals(Type.SINGLE, repository.getTaskById(3).getType());

        repository.removeTaskById(3);

        int expectedSizeListTaskAfterRemove = 2;

        assertEquals(expectedSizeListTaskAfterRemove, repository.getAllTasks().size());
    }
}
