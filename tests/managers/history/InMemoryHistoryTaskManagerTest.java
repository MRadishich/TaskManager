package managers.history;

import main.java.managers.InMemoryHistoryTaskManager;
import main.java.repository.InMemoryTaskRepository;
import main.java.tasks.Status;
import main.java.tasks.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryHistoryTaskManagerTest {
    @Test
    public void test1_shouldReturnEmptyHistoryList() {
        var manager = new InMemoryHistoryTaskManager(new InMemoryTaskRepository());

        assertTrue(manager.getHistory().isEmpty());
    }

    @Test
    public void test2_shouldSaveOnlyUniqTasks() {
        var taskRepository = new InMemoryTaskRepository();
        var historyManager = new InMemoryHistoryTaskManager(taskRepository);

        Task task1 = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));
        Task task2 = new Task(1, "Single Task #2", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));
        Task task3 = new Task(2, "Single Task #3", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));
        Task task4 = new Task(0, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        taskRepository.saveTask(task1);
        taskRepository.saveTask(task2);
        taskRepository.saveTask(task3);
        taskRepository.saveTask(task4);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);

        assertEquals(3, historyManager.getHistory().size());
        assertEquals(task2, historyManager.getHistory().get(0));
        assertEquals(task3, historyManager.getHistory().get(1));
        assertEquals(task4, historyManager.getHistory().get(2));
    }

    @Test
    public void test3_removeTaskById() {
        var taskRepository = new InMemoryTaskRepository();
        var historyManager = new InMemoryHistoryTaskManager(taskRepository);

        for (int i = 0; i < 10; i++) {
            Task task = new Task(i, "Single Task #1", "Simple single task", Status.NEW, Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));
            taskRepository.saveTask(task);
            historyManager.add(task);
        }

        historyManager.remove(9);
        historyManager.remove(5);
        historyManager.remove(0);

        assertEquals(7, historyManager.getHistory().size());
    }
}
