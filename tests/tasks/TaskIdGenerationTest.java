package tasks;

import main.java.tasks.TaskIdGeneration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskIdGenerationTest {
    @Test
    public void test1_shouldReturnNextFreeId() {
        TaskIdGeneration taskIdGeneration = new TaskIdGeneration();

        int expected = 0;

        assertEquals(expected, taskIdGeneration.getNextFreeId());
    }

    @Test
    public void test2_shouldReturnIncrementedId() {
        TaskIdGeneration taskIdGeneration = new TaskIdGeneration();
        taskIdGeneration.getNextFreeId();

        int expected = 1;

        assertEquals(taskIdGeneration.getNextFreeId(), expected);
    }

    @Test
    public void test3_shouldReturnNewValueAfterSetNextFreeId() {
        TaskIdGeneration taskIdGeneration = new TaskIdGeneration();
        taskIdGeneration.setNextFreeId(10);

        int expected = 10;

        assertEquals(expected, taskIdGeneration.getNextFreeId());
    }
}
