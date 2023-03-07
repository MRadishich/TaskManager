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
import main.java.tasks.Type;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {

    public TaskManager createTaskManager() {
        var taskRepository = new InMemoryTaskRepository();

        return new InMemoryTaskManager(
                new TaskIdGeneration(),
                new InMemoryTaskRepository(),
                new InMemoryHistoryTaskManager(taskRepository)
        );
    }

    public TaskManager createTaskManagerWithTasks() {
        final var taskRepository = new InMemoryTaskRepository();

        TaskManager manager = new InMemoryTaskManager(
                new TaskIdGeneration(),
                taskRepository,
                new InMemoryHistoryTaskManager(taskRepository)
        );

        manager.createTask(TaskDTO.getTaskDTO("null,EPIC,Epic #1,Simple Epic,null,540,2023-03-03T10:00,null", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,EPIC,Epic #2,Simple Epic,null,540,2023-03-03T10:00,null", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SINGLE,SingleTask #1,Simple SingleTask,NEW,540,2023-03-03T10:00,null", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SINGLE,SingleTask #2,Simple SingleTask,NEW,540,2023-03-03T10:00,null", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SUB,SubTask #1,SubTask #1 by Epic #1,NEW,540,2023-03-05T10:00,0", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SUB,SubTask #2,SubTask #2 by Epic #1,NEW,540,2023-03-05T10:00,0", ","));

        return manager;
    }

    @Test
    public void test1_shouldAddNewTask() {
        TaskManager manager = createTaskManager();

        Task task1 = manager.createTask(TaskDTO.getTaskDTO("null,EPIC,Epic #1,Simple Epic,null,540,2023-03-03T10:00,null", ","));
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        assertEquals(0, task1.getId(), "Id задач не совпадает.");
        assertEquals(1, manager.getAllTasks().size(), "Количество задач не совпадает.");
        assertEquals(epic, manager.getTaskById(0), "Задачи не совпадают.");
    }

    @Test
    public void test2_shouldReturnTaskById() {
        TaskManager manager = createTaskManager();

        manager.createTask(TaskDTO.getTaskDTO("null,EPIC,Epic #1,Simple Epic,null,540,2023-03-03T10:00,null", ","));
        Epic epic = new Epic(0, "Epic #1", "Simple Epic", Duration.ofMinutes(540L), LocalDateTime.parse("2023-03-03T10:00"));

        assertEquals(epic, manager.getTaskById(0));

        final TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class,
                () -> manager.getTaskById(1)
        );

        assertEquals("Задача с id 1 не найдена.", exception.getMessage(), "Сообщения об ошибке разные");
    }

    @Test
    public void test3_shouldReturnAllTask() {
        TaskManager manager = createTaskManagerWithTasks();

        assertEquals(6, manager.getAllTasks().size(), "Количество задач не совпадает.");
    }

    @Test
    public void test4_shouldReturnTaskByType() {
        TaskManager manager = createTaskManagerWithTasks();

        assertEquals(2, manager.getAllEpic().size(), "Количество эпиков не совпадает.");
        assertEquals(2, manager.getAllSubTasks().size(), "Количество подзадач не совпадает.");
        assertEquals(2, manager.getAllSingleTasks().size(), "Количество обычных задач не совпадает.");
    }

    @Test
    public void test5_shouldReturnSubTasksByEpicId() {
        TaskManager manager = createTaskManagerWithTasks();

        assertEquals(2, manager.getAllSubTasksByEpicId(0).size(), "Количество подзадач не совпадает.");
    }

    @Test
    public void test6_shouldReturnTaskViewHistory() {
        TaskManager manager = createTaskManagerWithTasks();

        Task task1 = manager.getTaskById(0);
        Task task2 = manager.getTaskById(2);
        Task task3 = manager.getTaskById(4);

        assertEquals(3, manager.getHistory().size());
        assertEquals(task1.getName(), manager.getHistory().get(0).getName(), "Задача в истории не соответствует просмотренной.");
        assertEquals(task2.getName(), manager.getHistory().get(1).getName(), "Задача в истории не соответствует просмотренной.");
        assertEquals(task3.getName(), manager.getHistory().get(2).getName(), "Задача в истории не соответствует просмотренной.");

    }

    @Test
    public void test7_shouldReturnTaskViewHistoryInActualState() {
        TaskManager manager = createTaskManagerWithTasks();

        manager.getTaskById(0);
        manager.getTaskById(2);
        manager.getTaskById(4);

        Task task1 = manager.updateTask(
                manager.createTask(
                        TaskDTO.getTaskDTO("0,EPIC,Updated Epic #1,Simple Epic,NEW,840,2023-03-03T10:00,null", ",")));

        Task task2 = manager.updateTask(
                manager.createTask(
                        TaskDTO.getTaskDTO("2,SINGLE,Updated SingleTask #1,Simple SingleTask,DONE,840,2023-03-03T10:00,null", ",")));

        Task task3 = manager.updateTask(
                manager.createTask(
                        TaskDTO.getTaskDTO("4,SUB,Updated SubTask #1,SubTask #1 by Epic #2,IN_PROGRESS,540,2023-03-05T10:00,1", ",")));

        assertEquals(task1, manager.getHistory().get(0), "Задачи не совпадают.");
        assertEquals(task2, manager.getHistory().get(1), "Задачи не совпадают.");
        assertEquals(task3, manager.getHistory().get(2), "Задачи не совпадают.");
    }

    @Test
    public void test8_UpdateSubTaskShouldBeReflectedInEpic() {
        TaskManager manager = createTaskManagerWithTasks();

        Task updatedTask = manager.updateTask(
                manager.createTask(
                        TaskDTO.getTaskDTO("4,SUB,Updated SubTask #1,SubTask #1 by Epic #2,IN_PROGRESS,540,2023-03-05T10:00,1", ",")));

        assertEquals(1, manager.getAllSubTasksByEpicId(0).size(), "Количество задач не совпадает.");
        assertEquals(1, manager.getAllSubTasksByEpicId(1).size(), "Количество задач не совпадает.");
        assertEquals(updatedTask, manager.getAllSubTasksByEpicId(1).get(0), "Задачи не совпадают.");
    }

    @Test
    public void test9_addNewTaskWithUnknownType() {
        TaskManager manager = createTaskManager();
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> manager.createTask(TaskDTO.getTaskDTO("null,FEATURE,Epic #1,Simple Epic,null,540,2023-03-03T10:00", ","))
        );

        assertEquals("Неизвестный тип задачи: FEATURE", exception.getMessage(), "Сообщения об ошибке не совпадают.");
    }

    @Test
    public void test10_addNewTaskWithUnknownStatus() {
        TaskManager manager = Managers.getDefaultTaskManager();
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> manager.createTask(TaskDTO.getTaskDTO("null,EPIC,Epic #1,Simple Epic,CANCEL,540,2023-03-03T10:00", ","))
        );

        assertEquals("Неизвестный статус: CANCEL", exception.getMessage(), "Сообщения об ошибке не совпадают.");
    }

    @Test
    public void test11_shouldRemoveTaskById() {
        TaskManager manager = createTaskManagerWithTasks();

        manager.removeTaskById(0);

        TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class,
                () -> manager.removeTaskById(0)
        );

        assertEquals("Задача с id 0 не найдена.", exception.getMessage(), "Сообщения об ошибке не совпадают.");
        assertEquals(Type.SINGLE, manager.getTaskById(4).getType(), "Типы задач не совпадют.");
        assertEquals(Type.SINGLE, manager.getTaskById(5).getType(), "Типы задач не совпадют.");
        assertEquals(1, manager.getAllEpic().size(), "Количество эпиков не сопадает");
        assertEquals(4, manager.getAllSingleTasks().size(), "Количество обычных задач не сопадает");
        assertTrue(manager.getAllSubTasks().isEmpty(), "Список с подзачами не пустой.");

    }
}
