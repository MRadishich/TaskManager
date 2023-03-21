package main.java.managers.http;

import com.google.gson.Gson;
import main.java.dto.TaskDTO;
import main.java.managers.HistoryManager;
import main.java.managers.Managers;
import main.java.managers.filebacked.FileBackedTasksManager;
import main.java.repository.TaskRepository;
import main.java.tasks.TaskIdGeneration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {
    private static final String KEY_FOR_SAVE_TASKS = "tasks";
    private static final String KEY_FOR_LOAD_HISTORY = "history";
    private final KVTaskClient client;
    private final Gson gson;

    public HttpTaskManager(
            TaskIdGeneration taskIdGeneration,
            TaskRepository taskRepository,
            HistoryManager historyManager,
            String urlKVServer
    ) {
        super(taskIdGeneration, taskRepository, historyManager);
        this.client = new KVTaskClient(urlKVServer);
        this.gson = Managers.getGson();
    }

    @Override
    protected void save() {
        saveTasks();
        saveHistory();
    }

    private void saveTasks() {
        client.put(
                KEY_FOR_SAVE_TASKS,
                gson.toJson(getAllTasks().stream()
                        .map(TaskDTO::toTaskDTO)
                        .collect(Collectors.toList()))
        );
    }

    private void saveHistory() {
        client.put(
                KEY_FOR_LOAD_HISTORY,
                gson.toJson(new ArrayList<>(getHistory()))
        );
    }

    public void load() {
        loadTasks();
        loadHistory();
    }

    private void loadTasks() {
        String tasks = client.load(HttpTaskManager.KEY_FOR_SAVE_TASKS);
        if (!tasks.isEmpty()) {
            TaskDTO[] taskDTOS = gson.fromJson(tasks, TaskDTO[].class);
            Arrays.stream(taskDTOS).forEach(this::createTask);
        }
    }

    private void loadHistory() {
        String history = client.load(HttpTaskManager.KEY_FOR_LOAD_HISTORY);
        int[] tasks = gson.fromJson(history, int[].class);
        Arrays.stream(tasks).forEach(id -> getHistoryManager().add(getTaskById(id)));
    }
}
