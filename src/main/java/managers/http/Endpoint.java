package main.java.managers.http;

import com.sun.net.httpserver.HttpExchange;

public enum Endpoint {
    GET_ALL_TASKS,
    GET_SINGLE_TASKS,
    GET_EPICS,
    GET_SUBTASKS,
    GET_TASK,
    POST_TASK,
    DELETE_TASK,
    DELETE_ALL_TASK,
    GET_SUBTASK_BY_EPIC,
    GET_HISTORY,
    UNKNOWN;

    public static Endpoint valueOf(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        switch (exchange.getRequestMethod()) {
            case "GET":
                switch (path) {
                    case "/tasks":
                        return GET_ALL_TASKS;
                    case "/tasks/task":
                        if (exchange.getRequestURI().getQuery() == null) {
                            return GET_SINGLE_TASKS;
                        }
                        return GET_TASK;
                    case "/tasks/epic":
                        return GET_EPICS;
                    case "/tasks/subtask":
                        return GET_SUBTASKS;
                    case "/tasks/history":
                        return GET_HISTORY;
                    case "/tasks/subtask/epic":
                        return GET_SUBTASK_BY_EPIC;
                }
                break;
            case "DELETE":
                if ("/tasks/task".equals(path)) {
                    if (exchange.getRequestURI().getQuery() == null) {
                        return DELETE_ALL_TASK;
                    }
                    return DELETE_TASK;
                }
                break;
            case "POST":
                if ("/tasks/task".equals(path)) {
                    return POST_TASK;
                }
                break;
        }
        return UNKNOWN;
    }
}
