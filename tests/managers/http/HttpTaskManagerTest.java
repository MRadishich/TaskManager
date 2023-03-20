package managers.http;

import main.java.dto.TaskDTO;
import main.java.managers.http.HttpTaskManager;
import main.java.managers.http.KVServer;
import main.java.managers.inmemory.InMemoryHistoryTaskManager;
import main.java.repository.InMemoryTaskRepository;
import main.java.repository.TaskRepository;
import main.java.tasks.TaskIdGeneration;
import managers.TaskManagerTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    private static final String URL_KVSERVER = "http://localhost:8078";
    private static final KVServer KV_SERVER;

    static {
        try {
            KV_SERVER = new KVServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String apiToken;

    @BeforeAll
    public static void startKVServer() {
        KV_SERVER.start();
    }

    @BeforeEach
    public void createStateManager() {
        TaskRepository repository = new InMemoryTaskRepository();
        HttpTaskManager manager = new HttpTaskManager(
                new TaskIdGeneration(),
                repository,
                new InMemoryHistoryTaskManager(repository),
                URL_KVSERVER
        );
        apiToken = manager.getClient().getApiToken();
        manager.createTask(TaskDTO.getTaskDTO("null,EPIC,Epic #1,Make 100 push up,null,null,null", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,EPIC,Epic #2,Make 500 push up,null,null,null", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SUB,SubTask #1,Make 50 push up,DONE,540,2023-04-01T09:00,0", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SUB,SubTask #2,Make 50 push up,DONE,540,2023-04-02T09:00,0", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SUB,SubTask #1,Make 100 push up,NEW,540,2023-04-03T09:00,1", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SUB,SubTask #2,Make 100 push up,NEW,540,2023-04-04T09:00,1", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SUB,SubTask #3,Make 100 push up,DONE,540,2023-04-05T09:00,1", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SUB,SubTask #4,Make 100 push up,IN_PROGRESS,540,2023-04-06T09:00,1", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SUB,SubTask #5,Make 100 push up,IN_PROGRESS,540,2023-04-07T09:00,1", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SINGLE,SingleTask #1,Buy a cake,NEW,60,2023-04-08T09:00,", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SINGLE,SingleTask #2,Buy a bread,NEW,60,2023-04-08T10:00,", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SINGLE,SingleTask #3,Buy a cheese,null,60,2023-04-08T12:00,", ","));
    }

    @AfterAll
    public static void stopKVServer() {
        KV_SERVER.stop();
    }

    @Test
    public void test1_shouldRestoreManagerStateFromServer() {
        TaskRepository repository = new InMemoryTaskRepository();
        HttpTaskManager manager = new HttpTaskManager(
                new TaskIdGeneration(),
                repository,
                new InMemoryHistoryTaskManager(repository),
                URL_KVSERVER
        );

        assertTrue(manager.getAllTasks().isEmpty());
        assertTrue(manager.getAllSingleTasks().isEmpty());
        assertTrue(manager.getAllEpic().isEmpty());
        assertTrue(manager.getAllSubTasks().isEmpty());

        manager.load(apiToken);

        assertEquals(12, manager.getAllTasks().size());
        assertEquals(2, manager.getAllEpic().size());
        assertEquals(7, manager.getAllSubTasks().size());
        assertEquals(3, manager.getAllSingleTasks().size());
    }
}
