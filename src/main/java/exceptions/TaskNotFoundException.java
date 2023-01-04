package main.java.exceptions;

public class TaskNotFoundException extends IllegalArgumentException {
    public TaskNotFoundException(int id) {
        super("Задача с id " + id + " не найдена.");
    }
}
