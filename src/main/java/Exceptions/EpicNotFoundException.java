package main.java.Exceptions;

public class EpicNotFoundException extends IllegalArgumentException {
    public EpicNotFoundException(int id) {
        super("Эпик с id " + id + " не найден.");
    }
}
