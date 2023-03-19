package main.java.managers.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import main.java.dto.TaskDTO;
import main.java.exceptions.EpicNotFoundException;
import main.java.exceptions.TaskNotFoundException;
import main.java.managers.TaskManager;
import main.java.managers.http.adapter.DurationAdapter;
import main.java.managers.http.adapter.LocalDateTimeAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final TaskManager manager;
    private final HttpServer httpServer;
    private final Gson gson;

    public HttpTaskServer(TaskManager manager) throws IOException {
        this.manager = manager;
        httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/tasks", this::serverTasks);
        gson = new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    public void start() {
        System.out.println("\nЗапускаем " + this.getClass().getSimpleName() + " на порту " + PORT);
        httpServer.start();
    }

    public void stop() {
        System.out.println("\nОстанавливаем " + this.getClass().getSimpleName() + " на порту " + PORT);
        httpServer.stop(0);
    }

    private void serverTasks(HttpExchange exchange) throws IOException {

        Endpoint endpoint = getEndpoint(exchange, exchange.getRequestMethod());
        switch (endpoint) {
            case GET_ALL_TASKS:
                System.out.println("\nGET_ALL_TASKS");
                writeResponse(exchange, gson.toJson(manager.getAllTaskByPriority()), 200);
                break;
            case GET_EPICS:
                System.out.println("\nGET_EPICS");
                writeResponse(exchange, gson.toJson(manager.getAllEpic()), 200);
                break;
            case GET_SUBTASKS:
                System.out.println("\nGET_SUBTASKS");
                writeResponse(exchange, gson.toJson(manager.getAllSubTasks()), 200);
                break;
            case GET_SINGLE_TASKS:
                System.out.println("\nGET_SINGLE_TASKS");
                writeResponse(exchange, gson.toJson(manager.getAllSingleTasks()), 200);
                break;
            case GET_TASK:
                System.out.println("\nGET_TASK");
                Optional<Integer> taskIdOpt = getTaskId(exchange);

                if (taskIdOpt.isEmpty()) {
                    writeResponse(exchange, "Некорректный идентификатор задачи", 404);
                    return;
                }

                int taskId = taskIdOpt.get();

                try {
                    writeResponse(exchange, gson.toJson(manager.getTaskById(taskId)), 200);
                } catch (TaskNotFoundException e) {
                    writeResponse(exchange, "Задача с id " + taskId + " не найдена", 404);
                }
                break;
            case GET_SUBTASK_BY_EPIC:
                System.out.println("\nGET_SUBTASK_BY_EPIC");
                Optional<Integer> epicIdOpt = getTaskId(exchange);

                if (epicIdOpt.isEmpty()) {
                    writeResponse(exchange, "Некорректный идентификатор эпика", 404);
                    return;
                }

                int epicId = epicIdOpt.get();

                try {
                    writeResponse(exchange, gson.toJson(manager.getAllSubTasksByEpicId(epicId)), 200);
                } catch (EpicNotFoundException e) {
                    writeResponse(exchange, "Эпик с id " + epicId + " не найден", 404);
                }
                break;
            case GET_HISTORY:
                System.out.println("\nGET_HISTORY");
                writeResponse(exchange, gson.toJson(manager.getHistory()), 200);
                break;
            case DELETE_ALL_TASK:
                System.out.println("\nDELETE_ALL_TASK");
                manager.removeAllTasks();
                writeResponse(exchange, "Все задачи удалены", 200);
                break;
            case DELETE_TASK:
                System.out.println("\nDELETE_TASK");
                taskIdOpt = getTaskId(exchange);

                if (taskIdOpt.isEmpty()) {
                    writeResponse(exchange, "Некорректный идентификатор эпика", 404);
                    return;
                }

                taskId = taskIdOpt.get();

                try {
                    manager.removeTaskById(taskId);
                    writeResponse(exchange, "Задача с id " + taskId + " удалена", 200);
                } catch (TaskNotFoundException e) {
                    writeResponse(exchange, "Задача с id " + taskId + " не найдена", 404);
                }
                break;
            case POST_TASK:
                System.out.println("\nPOST_TASK");
                InputStream body = exchange.getRequestBody();
                String str = new String(body.readAllBytes(), StandardCharsets.UTF_8);

                try {
                    manager.createTask(gson.fromJson(str, TaskDTO.class));
                    writeResponse(exchange, "Задача успешно создана", 200);
                } catch (JsonSyntaxException e) {
                    System.out.println("\nПри чтении JSON произошла ошибка: " + e.getMessage());
                    writeResponse(exchange, "Получен некорректный JSON", 400);
                } catch (RuntimeException e) {
                    System.out.println("\nПри чтении JSON произошла ошибка: " + e.getMessage());
                    writeResponse(exchange, e.getMessage(), 404);
                }
                break;
            case UNKNOWN:
                System.out.println("\nUNKNOWN");
                writeResponse(exchange, "Некорректный запрос", 400);
                break;
        }
    }

    private Endpoint getEndpoint(HttpExchange exchange, String requestMethod) {
        String[] path = exchange.getRequestURI().getPath().split("/");
        switch (requestMethod) {
            case "GET":
                if (path.length == 2) {
                    return Endpoint.GET_ALL_TASKS;
                }

                if (path.length == 3) {
                    switch (path[2]) {
                        case "task":
                            if (exchange.getRequestURI().getQuery() == null) {
                                return Endpoint.GET_SINGLE_TASKS;
                            }
                            return Endpoint.GET_TASK;
                        case "epic":
                            return Endpoint.GET_EPICS;
                        case "subtask":
                            return Endpoint.GET_SUBTASKS;
                        case "history":
                            return Endpoint.GET_HISTORY;
                    }
                }

                if (path.length == 4) {
                    return Endpoint.GET_SUBTASK_BY_EPIC;
                }
                break;
            case "DELETE":
                if (path.length == 3 && "task".equals(path[2])) {
                    if (exchange.getRequestURI().getQuery() == null) {
                        return Endpoint.DELETE_ALL_TASK;
                    }
                    return Endpoint.DELETE_TASK;
                }
                break;
            case "POST":
                if (path.length == 3 && "task".equals(path[2])) {
                    return Endpoint.POST_TASK;
                }
                break;
        }
        return Endpoint.UNKNOWN;
    }

    private void writeResponse(HttpExchange exchange, String response, int responseCode) throws IOException {
        if (response.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        System.out.println("Код ответа: " + responseCode);
        exchange.close();
    }

    private Optional<Integer> getTaskId(HttpExchange exchange) {
        String[] params = exchange.getRequestURI().getQuery().split("&");
        Map<String, String> queries = new HashMap<>();
        for (String param : params) {
            String[] query = param.split("=");
            if (query.length > 1) {
                queries.put(query[0], query[1]);
            }
        }
        try {
            if (queries.containsKey("id")) {
                return Optional.of(Integer.parseInt(queries.get("id")));
            }
            return Optional.empty();
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
