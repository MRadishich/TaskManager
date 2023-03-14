package main.java.managers.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import main.java.dto.TaskDTO;
import main.java.managers.Managers;
import main.java.managers.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private TaskManager manager;
    private final HttpServer httpServer;
    private static final Gson gson = new Gson();

    public HttpTaskServer() throws IOException {
        manager = Managers.getFileBackedTasksManager();
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", this::serverTasks);
    }

    public void run() {
        manager.createTask(
                TaskDTO.getTaskDTO("0,EPIC,Epic #1,Make 100 push up,DONE,1080,2023-04-01T09:00,null", ",")
        );
        System.out.println("Сервер запущен на порту " + PORT);
        httpServer.start();
    }

    private void serverTasks(HttpExchange exchange) throws IOException {

        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_ALL_TASKS -> writeResponse(exchange, gson.toJson(manager.getAllTasks()), 200);
            case GET_EPICS -> writeResponse(exchange, gson.toJson(manager.getAllEpic()), 200);
            case GET_SUBTASKS -> writeResponse(exchange, gson.toJson(manager.getAllSubTasks()), 200);
            case GET_SINGLE_TASKS -> writeResponse(exchange, gson.toJson(manager.getAllSingleTasks()), 200);
            case UNKNOWN -> writeResponse(exchange, "Некорректный запрос", 400);
        }
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] path = requestPath.split("/");
        System.out.println(requestPath);
        System.out.println(manager.getAllTasks());
        if (requestMethod.equals("GET")) {
            if (path.length == 2) {
                return Endpoint.GET_ALL_TASKS;
            } else if (path.length == 3) {
                switch (path[2]) {
                    case "task" -> {
                        return Endpoint.GET_SINGLE_TASKS;
                    }
                    case "epic" -> {
                        return Endpoint.GET_EPICS;
                    }
                    case "subtask" -> {
                        return Endpoint.GET_SUBTASKS;
                    }
                }
            }
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

}
