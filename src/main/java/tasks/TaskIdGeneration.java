package main.java.tasks;

public class TaskIdGeneration {
    private int nextFreeId;

    public TaskIdGeneration() {
        this.nextFreeId = 0;
    }

    public int getNextFreeId() {
        return nextFreeId++;
    }

    public void setNextFreeId(int nextFreeId) {
        this.nextFreeId = nextFreeId;
    }
}
