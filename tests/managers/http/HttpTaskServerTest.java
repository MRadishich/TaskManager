package managers.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.java.dto.TaskDTO;
import main.java.managers.TaskManager;
import main.java.managers.http.HttpTaskManager;
import main.java.managers.http.HttpTaskServer;
import main.java.managers.http.KVServer;
import main.java.managers.http.adapter.DurationAdapter;
import main.java.managers.http.adapter.LocalDateTimeAdapter;
import main.java.managers.inmemory.InMemoryHistoryTaskManager;
import main.java.repository.InMemoryTaskRepository;
import main.java.repository.TaskRepository;
import main.java.tasks.TaskIdGeneration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpTaskServerTest {
    private static final String URL_KVSERVER = "http://localhost:8078";
    private HttpTaskServer server;
    private TaskManager manager;
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new GsonBuilder().
            serializeNulls()
            .setPrettyPrinting()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    @BeforeAll
    public static void startKVServer() throws IOException {
        new KVServer().start();
    }

    @BeforeEach
    public void start() {
        TaskRepository taskRepository = new InMemoryTaskRepository();
        manager = new HttpTaskManager(
                new TaskIdGeneration(),
                taskRepository,
                new InMemoryHistoryTaskManager(taskRepository),
                URL_KVSERVER
        );

        try {
            server = new HttpTaskServer(manager);
            server.start();
        } catch (IOException e) {
            System.out.println("При запуске сервера произошла ошибка: " + e.getMessage());
        }
    }

    @AfterEach
    public void stop() {
        server.stop();
    }

    @Test
    public void test1_addNewTask() throws IOException, InterruptedException {
        TaskDTO task = TaskDTO.getTaskDTO("null,SINGLE,SingleTask #1,Buy a cake,NEW,60,2023-04-08T09:00", ",");
        String json = gson.toJson(task);
        final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create("http://localhost:8080/tasks/task/"))
                .build();

        HttpResponse<String> response = client.send(request, handler);

        assertEquals(200, response.statusCode(), "Статус ответа не совпадает.");
        assertEquals(1, manager.getAllTasks().size(), "Количество задач не совпадает.");
        assertEquals(1, manager.getAllSingleTasks().size(), "Количество обычных задач не совпадает.");
    }

    @Test
    public void test2_shouldReturnAllTasksByPriority() throws IOException, InterruptedException {
        manager.createTask(TaskDTO.getTaskDTO("null,EPIC,Epic #1,Make 100 push up,null,null,null", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,EPIC,Epic #2,Make 500 push up,null,null,null", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SUB,SubTask #1,Make 50 push up,DONE,540,2023-04-01T09:00,0", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SUB,SubTask #2,Make 50 push up,DONE,540,2023-04-02T09:00,0", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SINGLE,SingleTask #1,Buy a cake,NEW,60,2023-04-08T09:00,", ","));

        final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/tasks"))
                .build();

        HttpResponse<String> response = client.send(request, handler);

        assertEquals(200, response.statusCode(), "Статус ответа не совпадает");
        assertEquals(gson.toJson(manager.getAllTaskByPriority()), response.body(), "Тело ответа не совпадает");
    }

    @Test
    public void test3_shouldReturnAllSingleTask() throws IOException, InterruptedException {
        manager.createTask(TaskDTO.getTaskDTO("null,EPIC,Epic #1,Make 100 push up,null,null,null", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SUB,SubTask #1,Make 50 push up,DONE,540,2023-04-01T09:00,0", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SINGLE,SingleTask #1,Buy a cake,NEW,60,2023-04-08T09:00,", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SINGLE,SingleTask #2,Buy a bread,NEW,60,2023-04-08T06:00,", ","));

        final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/tasks/task"))
                .build();

        HttpResponse<String> response = client.send(request, handler);

        assertEquals(200, response.statusCode(), "Статус ответа не совпадает");
        assertEquals(gson.toJson(manager.getAllSingleTasks()), response.body(), "Тело ответа не совпадает");
    }

    @Test
    public void test4_shouldReturnAllEpic() throws IOException, InterruptedException {
        manager.createTask(TaskDTO.getTaskDTO("null,EPIC,Epic #1,Make 100 push up,null,null,null", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,EPIC,Epic #2,Make 500 push up,null,null,null", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SUB,SubTask #1,Make 50 push up,DONE,540,2023-04-01T09:00,0", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SUB,SubTask #2,Make 50 push up,DONE,540,2023-04-02T09:00,0", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SINGLE,SingleTask #1,Buy a cake,NEW,60,2023-04-08T09:00,", ","));

        final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/tasks/epic"))
                .build();

        HttpResponse<String> response = client.send(request, handler);

        assertEquals(200, response.statusCode(), "Статус ответа не совпадает");
        assertEquals(gson.toJson(manager.getAllEpic()), response.body(), "Тело ответа не совпадает");
    }

    @Test
    public void test5_shouldReturnAllSubTasks() throws IOException, InterruptedException {
        manager.createTask(TaskDTO.getTaskDTO("null,EPIC,Epic #1,Make 100 push up,null,null,null", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,EPIC,Epic #2,Make 500 push up,null,null,null", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SUB,SubTask #1,Make 50 push up,DONE,540,2023-04-01T09:00,0", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SUB,SubTask #2,Make 50 push up,DONE,540,2023-04-02T09:00,0", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SUB,SubTask #1,Make 100 push up,NEW,540,2023-04-03T09:00,1", ","));

        final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/tasks/subtask"))
                .build();

        HttpResponse<String> response = client.send(request, handler);

        assertEquals(200, response.statusCode(), "Статус ответа не совпадает");
        assertEquals(gson.toJson(manager.getAllSubTasks()), response.body(), "Тело ответа не совпадает");
    }

    @Test
    public void test6_shouldReturnTasksById() throws IOException, InterruptedException {
        manager.createTask(TaskDTO.getTaskDTO("null,EPIC,Epic #1,Make 100 push up,null,null,null", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,EPIC,Epic #2,Make 500 push up,null,null,null", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SUB,SubTask #1,Make 50 push up,DONE,540,2023-04-01T09:00,0", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SUB,SubTask #2,Make 50 push up,DONE,540,2023-04-02T09:00,0", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SINGLE,SingleTask #1,Buy a cake,NEW,60,2023-04-08T09:00,", ","));

        final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/tasks/task?id=0"))
                .build();

        HttpResponse<String> response = client.send(request, handler);

        assertEquals(200, response.statusCode(), "Статус ответа не совпадает");
        assertEquals(gson.toJson(manager.getTaskById(0)), response.body(), "Тело ответа не совпадает");
    }

    @Test
    public void test7_shouldReturnSubTasksByEpicId() throws IOException, InterruptedException {
        manager.createTask(TaskDTO.getTaskDTO("null,EPIC,Epic #1,Make 100 push up,null,null,null", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,EPIC,Epic #2,Make 500 push up,null,null,null", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SUB,SubTask #1,Make 50 push up,DONE,540,2023-04-01T09:00,0", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SUB,SubTask #2,Make 50 push up,DONE,540,2023-04-02T09:00,0", ","));

        HttpResponse.BodyHandler<String> handlers = HttpResponse.BodyHandlers.ofString();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/tasks/subtask/epic?id=0"))
                .build();

        HttpResponse<String> response = client.send(request, handlers);

        assertEquals(200, response.statusCode(), "Статус ответа не совпадает");
        assertEquals(gson.toJson(manager.getAllSubTasksByEpicId(0)), response.body(), "Тело ответа не совпадает");
    }


    @Test
    public void test8_shouldDeleteTaskById() throws IOException, InterruptedException {
        manager.createTask(TaskDTO.getTaskDTO("null,EPIC,Epic #1,Make 100 push up,null,null,null", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,EPIC,Epic #2,Make 500 push up,null,null,null", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SUB,SubTask #1,Make 50 push up,DONE,540,2023-04-01T09:00,0", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SUB,SubTask #2,Make 50 push up,DONE,540,2023-04-02T09:00,0", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SINGLE,SingleTask #1,Buy a cake,NEW,60,2023-04-08T09:00,", ","));

        assertEquals(5, manager.getAllTasks().size(), "Количество задач не совпадает");
        assertEquals(1, manager.getAllSingleTasks().size(), "Количество обычных задач не совпадает");

        final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create("http://localhost:8080/tasks/task?id=4"))
                .build();

        HttpResponse<String> response = client.send(request, handler);

        assertEquals(200, response.statusCode(), "Статус ответа не совпадает");
        assertEquals("Задача с id 4 удалена", response.body(), "Тело ответа не совпадает");
        assertEquals(4, manager.getAllTasks().size(), "Количество задач не совпадает");
        assertTrue(manager.getAllSingleTasks().isEmpty(), "Список обычных задач не пустой");
    }

    @Test
    public void test9_shouldDeleteAllTasks() throws IOException, InterruptedException {
        manager.createTask(TaskDTO.getTaskDTO("null,EPIC,Epic #1,Make 100 push up,null,null,null", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,EPIC,Epic #2,Make 500 push up,null,null,null", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SUB,SubTask #1,Make 50 push up,DONE,540,2023-04-01T09:00,0", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SUB,SubTask #2,Make 50 push up,DONE,540,2023-04-02T09:00,0", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SINGLE,SingleTask #1,Buy a cake,NEW,60,2023-04-08T09:00,", ","));

        assertEquals(5, manager.getAllTasks().size(), "Количество задач не совпадает");
        assertEquals(1, manager.getAllSingleTasks().size(), "Количество обычных задач не совпадает");

        final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create("http://localhost:8080/tasks/task"))
                .build();

        HttpResponse<String> response = client.send(request, handler);

        assertEquals(200, response.statusCode(), "Статус ответа не совпадает");
        assertEquals("Все задачи удалены", response.body(), "Тело ответа не совпадает");
        assertTrue(manager.getAllTasks().isEmpty(), "Список задач не пустой");
    }

    @Test
    public void test10_shouldReturnHistory() throws IOException, InterruptedException {
        manager.createTask(TaskDTO.getTaskDTO("null,EPIC,Epic #1,Make 100 push up,null,null,null", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,EPIC,Epic #2,Make 500 push up,null,null,null", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SUB,SubTask #1,Make 50 push up,DONE,540,2023-04-01T09:00,0", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SUB,SubTask #2,Make 50 push up,DONE,540,2023-04-02T09:00,0", ","));
        manager.createTask(TaskDTO.getTaskDTO("null,SINGLE,SingleTask #1,Buy a cake,NEW,60,2023-04-08T09:00,", ","));

        assertTrue(manager.getHistory().isEmpty(), "История не пустая");

        manager.getTaskById(2);
        manager.getTaskById(3);

        final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/tasks/history"))
                .build();

        HttpResponse<String> response = client.send(request, handler);

        assertEquals(200, response.statusCode(), "Статус ответа не совпадает");
        assertEquals(gson.toJson(manager.getHistory()), response.body(), "Тело ответа не совпадает");
    }

    @Test
    public void test11_shouldReturn400StatusIfURLInvalid() throws IOException, InterruptedException {
        final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/tasks/rask"))
                .build();

        HttpResponse<String> response = client.send(request, handler);

        assertEquals(400, response.statusCode(), "Статус ответа отличается");
        assertEquals("Некорректный запрос", response.body());
    }

    @Test
    public void test12_shouldReturn400StatusIfMethodInvalid() throws IOException, InterruptedException {
        final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create("http://localhost:8080/tasks/task/epic?id=0"))
                .build();

        HttpResponse<String> response = client.send(request, handler);

        assertEquals(400, response.statusCode());
        assertEquals("Некорректный запрос", response.body());
    }
}
