package main.java.managers.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.java.dto.TaskDTO;
import main.java.managers.HistoryManager;
import main.java.managers.filebacked.FileBackedTasksManager;
import main.java.managers.http.adapter.DurationAdapter;
import main.java.managers.http.adapter.LocalDateTimeAdapter;
import main.java.repository.TaskRepository;
import main.java.tasks.TaskIdGeneration;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient client;
    private final Gson gson;

    public KVTaskClient getClient() {
        return client;
    }

    public HttpTaskManager(
            TaskIdGeneration taskIdGeneration,
            TaskRepository taskRepository,
            HistoryManager historyManager,
            String urlKVServer
    ) {
        super(taskIdGeneration, taskRepository, historyManager);
        this.client = new KVTaskClient(urlKVServer);
        this.gson = new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    @Override
    protected void save() {
        try {
            client.put(
                    client.getApiToken(),
                    gson.toJson(getAllTasks().stream()
                            .map(TaskDTO::toTaskDTO)
                            .collect(Collectors.toList()))
            );
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void load(String key) {
        String tasks = client.load(key);
        if (!tasks.isEmpty()) {
            TaskDTO[] taskDTOS = gson.fromJson(tasks, TaskDTO[].class);
            Arrays.stream(taskDTOS).forEach(this::createTask);
        }
    }
}
