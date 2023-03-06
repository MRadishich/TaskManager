package managers.filebacked.inmemory;

import main.java.dto.TaskDTO;
import main.java.exceptions.TaskNotFoundException;
import main.java.managers.InMemoryHistoryTaskManager;
import main.java.managers.InMemoryTaskManager;
import main.java.managers.Managers;
import main.java.managers.TaskManager;
import main.java.repository.InMemoryTaskRepository;
import main.java.tasks.Epic;
import main.java.tasks.Task;
import main.java.tasks.TaskIdGeneration;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InMemoryTaskManagerTest {

    public TaskManager createTaskManager() {
        var taskRepository = new InMemoryTaskRepository();

        //        manager.createTask(TaskDTO.createTaskDTO("null,EPIC,Epic #1,Simple Epic,NEW,540,2023-03-03T10:00", ","));
//        manager.createTask(TaskDTO.createTaskDTO("null,EPIC,Epic #2,Simple Epic,NEW,540,2023-03-03T10:00", ","));
//        manager.createTask(TaskDTO.createTaskDTO("null,SINGLE,SingleTask #1,Simple SingleTask,NEW,540,2023-03-03T10:00", ","));
//        manager.createTask(TaskDTO.createTaskDTO("null,SINGLE,SingleTask #2,Simple SingleTask,NEW,540,2023-03-03T10:00", ","));
//        manager.createTask(TaskDTO.createTaskDTO("null,SUB,SubTask #1,SubTask #1 by Epic #1,NEW,540,2023-03-05T10:00,0", ","));
//        manager.createTask(TaskDTO.createTaskDTO("null,SUB,SubTask #2,SubTask #1 by Epic #1,NEW,540,2023-03-05T10:00,0", ","));

        return new InMemoryTaskManager(
                new TaskIdGeneration(),
                new InMemoryTaskRepository(),
                new InMemoryHistoryTaskManager(taskRepository)
        );
    }

    @Test
    public void addNewTask() {
        TaskManager manager = createTaskManager();

        Task task1 = manager.createTask(TaskDTO.getTaskDTO("null,EPIC,Epic #1,Simple Epic,null,540,2023-03-03T10:00", ","));
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        assertEquals(0, task1.getId(), "Id задач не совпадает.");
        assertEquals(1, manager.getAllTasks().size(), "Количество задач не совпадает.");
        assertEquals(epic, manager.getTaskById(0), "Задачи не совпадают.");
    }

    @Test
    public void getTaskById() {
        TaskManager manager = createTaskManager();

        manager.createTask(TaskDTO.getTaskDTO("null,EPIC,Epic #1,Simple Epic,null,540,2023-03-03T10:00", ","));
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        assertEquals(epic, manager.getTaskById(0));

        final TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class,
                () -> manager.getTaskById(1)
        );

        assertEquals("Задача с id 1 не найдена.", exception.getMessage(), "Сообщения об ошибке разные");
    }

    @Test
    public void addNewTaskWithUnknownType() {
        TaskManager manager = createTaskManager();
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> manager.createTask(TaskDTO.getTaskDTO("null,FEATURE,Epic #1,Simple Epic,null,540,2023-03-03T10:00", ","))
        );

        assertEquals("Неизвестный тип задачи: FEATURE", exception.getMessage(), "Сообщения об ошибке разные");
    }

    @Test
    public void addNewTaskWithUnknownStatus() {
        TaskManager manager = Managers.getDefaultTaskManager();
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> manager.createTask(TaskDTO.getTaskDTO("null,EPIC,Epic #1,Simple Epic,CANCEL,540,2023-03-03T10:00", ","))
        );

        assertEquals("Неизвестный статус: CANCEL", exception.getMessage(), "Сообщения об ошибке разные");
    }
}
