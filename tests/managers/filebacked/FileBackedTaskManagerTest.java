package managers.filebacked;

import main.java.dto.TaskDTO;
import main.java.managers.Managers;
import main.java.managers.TaskManager;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest {

    @Test
    public void test1_shouldCreateTasksAndReturnTasks() {
        Path path = Paths.get("FileBackedTasksManagerTest.csv");
        TaskManager manager = Managers.getFileBackedTasksManager(path.toFile());

        try (BufferedReader br = new BufferedReader(new FileReader("tests/managers/filebacked/files/test1.csv"))) {
            while (br.ready()) {
                String line = br.readLine();
                manager.createTask(TaskDTO.getTaskDTO(line, ","));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        int expectedNumberOfEpic = 1;
        int expectedNumberOfSingleTask = 1;
        int expectedNumberOfSubTask = 1;

        assertEquals(expectedNumberOfEpic, manager.getAllEpic().size());
        assertEquals(expectedNumberOfSingleTask, manager.getAllSingleTasks().size());
        assertEquals(expectedNumberOfSubTask, manager.getAllSubTasks().size());
    }
}
