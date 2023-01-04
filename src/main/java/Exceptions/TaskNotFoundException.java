package main.java.Exceptions;

public class TaskNotFoundException extends IllegalArgumentException {
    public TaskNotFoundException(int id) {
        super("Задача с id " + id + " не найдена.");
    }
}
