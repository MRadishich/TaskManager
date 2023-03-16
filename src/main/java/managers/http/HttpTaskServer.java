package main.java.managers.http;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import main.java.dto.TaskDTO;
import main.java.exceptions.EpicNotFoundException;
import main.java.exceptions.TaskNotFoundException;
import main.java.managers.TaskManager;
import main.java.managers.filebacked.FileBackedTasksManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final TaskManager manager;
    private final HttpServer httpServer;
    private final Gson gson;

    public HttpTaskServer() throws IOException {
        manager = FileBackedTasksManager.loadFromFile(new File("saved2.csv"));
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", this::serverTasks);
        gson = new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    public void run() {
        System.out.println("Сервер запущен на порту " + PORT);
        httpServer.start();
    }

    private void serverTasks(HttpExchange exchange) throws IOException {

        Endpoint endpoint = getEndpoint(exchange, exchange.getRequestMethod());

        switch (endpoint) {
            case GET_ALL_TASKS:
                System.out.println("Получен запрос на предоставление всех задач");
                writeResponse(exchange, gson.toJson(manager.getAllTaskByPriority()), 200);
                break;
            case GET_EPICS:
                System.out.println("Получен запрос на предоставление всех эпиков");
                writeResponse(exchange, gson.toJson(manager.getAllEpic()), 200);
                break;
            case GET_SUBTASKS:
                System.out.println("Получен запрос на предоставление всех подзадач");
                writeResponse(exchange, gson.toJson(manager.getAllSubTasks()), 200);
                break;
            case GET_SINGLE_TASKS:
                System.out.println("Получен запрос на предоставление всех обычных задач");
                writeResponse(exchange, gson.toJson(manager.getAllSingleTasks()), 200);
                break;
            case GET_TASK:
                System.out.println("Получен запрос на предоставление задачи по id");
                Optional<Integer> taskIdOpt = getTaskId(exchange);

                if (taskIdOpt.isEmpty()) {
                    writeResponse(exchange, "Некорректный идентификатор задачи", 404);
                    return;
                }

                int taskId = taskIdOpt.get();

                try {
                    writeResponse(exchange, gson.toJson(manager.getTaskById(taskId)), 400);
                } catch (TaskNotFoundException e) {
                    writeResponse(exchange, "Задача с id " + taskId + " не найдена", 404);
                }
                break;
            case GET_SUBTASK_BY_EPIC:
                System.out.println("Получен запрос на предоставление подзадач эпика");
                Optional<Integer> epicIdOpt = getTaskId(exchange);

                if (epicIdOpt.isEmpty()) {
                    writeResponse(exchange, "Некорректный идентификатор эпика", 404);
                    return;
                }

                int epicId = epicIdOpt.get();

                try {
                    writeResponse(exchange, gson.toJson(manager.getAllSubTasksByEpicId(epicId)), 400);
                } catch (EpicNotFoundException e) {
                    writeResponse(exchange, "Эпик с id " + epicId + " не найден", 404);
                }
                break;
            case GET_HISTORY:
                System.out.println("Получен запрос на предоставление истории просмотров задач");
                writeResponse(exchange, gson.toJson(manager.getHistory()), 200);
                break;
            case DELETE_ALL_TASK:
                System.out.println("Получен запрос на удаление всех задач");
                manager.getAllTasks();
                writeResponse(exchange, "Все задачи удалены", 200);
                break;
            case DELETE_TASK:
                System.out.println("Получен запрос на удаление задачи");
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
                System.out.println("Получен запрос на добавление задачи");
                InputStream body = exchange.getRequestBody();
                String str = new String(body.readAllBytes(), StandardCharsets.UTF_8);

                try {
                    manager.createTask(gson.fromJson(str, TaskDTO.class));
                    writeResponse(exchange, "Задача успешно создана", 200);
                } catch (JsonSyntaxException e) {
                    System.out.println(e.getMessage());
                    writeResponse(exchange, "Получен некорректный JSON", 400);
                } catch (RuntimeException e) {
                    writeResponse(exchange, e.getMessage(), 404);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            case UNKNOWN:
                System.out.println("Получен неизвестный запрос");
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
                } else if (path.length == 3) {
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
                } else if (path.length == 4) {
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

    static class DurationAdapter extends TypeAdapter<Duration> {
        @Override
        public void write(final JsonWriter jsonWriter, final Duration duration) throws IOException {
            jsonWriter.value(duration.toMinutes());
        }
        @Override
        public Duration read(final JsonReader jsonReader) throws IOException {
            return Duration.ofMinutes(jsonReader.nextLong());
        }
    }

    static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
        @Override
        public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
            jsonWriter.value(localDateTime.format(DateTimeFormatter.ISO_DATE_TIME));
        }

        @Override
        public LocalDateTime read(final JsonReader jsonReader) throws IOException {
            return LocalDateTime.parse(jsonReader.nextString());
        }
    }
}
