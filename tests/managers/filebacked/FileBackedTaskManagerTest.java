package managers.filebacked;

import main.java.dto.TaskDTO;
import main.java.managers.filebacked.FileBackedTasksManager;
import main.java.managers.TaskManager;
import main.java.tasks.Status;
import main.java.tasks.Task;
import managers.TaskManagerTest;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @Test
    public void test1_loadFileBackedTaskManagerWithTaskAndHistory() {
        TaskManager manager = FileBackedTasksManager.loadFromFile(new File("tests/managers/filebacked/resources/TaskToLoadWithHistory.csv"));

        assertEquals(12, manager.getAllTasks().size(), "Количество задач не совпадает.");
        assertEquals(2, manager.getAllEpic().size(), "Количество эпиков не совпадает.");
        assertEquals(7, manager.getAllSubTasks().size(), "Количество подзадач не совпадает.");
        assertEquals(3, manager.getAllSingleTasks().size(), "Количество обычных задач не совпадает.");

        assertEquals(5, manager.getHistory().size(), "Количество задач в истории просмотров отличается.");

        assertEquals(Status.DONE, manager.getTaskById(0).getStatus(), "Статус отличается.");
        assertEquals(Status.IN_PROGRESS, manager.getTaskById(1).getStatus(), "Статус отличается.");

        assertEquals(1080, manager.getTaskById(0).getDuration().toMinutes(), "Дюрация первого эпика не совпадает.");
        assertEquals(LocalDateTime.parse("2023-04-01T09:00"), manager.getTaskById(0).getStartTime(), "Дата начала первого эпика не совпадает.");
        assertEquals(LocalDateTime.parse("2023-04-02T18:00"), manager.getTaskById(0).getEndTime(), "Дата окончания первого эпика не совпадает.");


        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> manager.createTask(
                        TaskDTO.getTaskDTO(
                                "null,SINGLE,SingleTask #4,Simple SubTask,NEW,540,2023-04-02T16:00,0", ","
                        )
                )
        );

        assertEquals("Невозможно создать задачу \"SingleTask #4\" т.к. она пересекается с другой задачей.", exception.getMessage(), "Исключения отличаются.");

        manager.removeTaskById(3);

        manager.createTask(
                TaskDTO.getTaskDTO(
                        "null,SINGLE,SingleTask #4,Simple SubTask,NEW,540,2023-04-02T16:00,0", ","
                )
        );

        assertEquals(12, manager.getAllTasks().size());

        Task task = new Task(
                10,
                "Updated SingleTask #2",
                "Buy a bread",
                Status.DONE,
                Duration.ofMinutes(60),
                LocalDateTime.parse("2023-04-08T10:00")
        );

        manager.updateTask(task);

        assertEquals(task.getName(), manager.getTaskById(10).getName());
        assertEquals(task.getStatus(), manager.getTaskById(10).getStatus());

        manager.removeAllTasks();

        assertTrue(manager.getAllTasks().isEmpty());
        assertTrue(manager.getHistory().isEmpty());
        assertTrue(manager.getAllTaskByPriority().isEmpty());
    }

    @Test
    public void test2_loadFileBackedTaskManagerWithoutHistory() {
        TaskManager manager = FileBackedTasksManager.loadFromFile(new File("tests/managers/filebacked/resources/TaskToLoad.csv"));

        assertEquals(12, manager.getAllTasks().size(), "Количество задач не совпадает.");
        assertEquals(2, manager.getAllEpic().size(), "Количество эпиков не совпадает.");
        assertEquals(7, manager.getAllSubTasks().size(), "Количество подзадач не совпадает.");
        assertEquals(3, manager.getAllSingleTasks().size(), "Количество обычных задач не совпадает.");

        assertTrue(manager.getHistory().isEmpty(), "История не пустая.");

        assertEquals(Status.DONE, manager.getTaskById(0).getStatus(), "Статус отличается.");
        assertEquals(Status.IN_PROGRESS, manager.getTaskById(1).getStatus(), "Статус отличается.");

        assertEquals(1080, manager.getTaskById(0).getDuration().toMinutes(), "Дюрация первого эпика не совпадает.");
        assertEquals(LocalDateTime.parse("2023-04-01T09:00"), manager.getTaskById(0).getStartTime(), "Дата начала первого эпика не совпадает.");
        assertEquals(LocalDateTime.parse("2023-04-02T18:00"), manager.getTaskById(0).getEndTime(), "Дата окончания первого эпика не совпадает.");
    }

    @Test
    public void test3_loadFileBackedTaskManagerWithEmptyFile() {
        TaskManager manager = FileBackedTasksManager.loadFromFile(new File("tests/managers/filebacked/resources/Empty.csv"));

        assertTrue(manager.getAllTasks().isEmpty(), "Список задач не пустой.");

        assertTrue(manager.getHistory().isEmpty(), "История не пустая.");
    }
}
