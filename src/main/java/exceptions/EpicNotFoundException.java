package main.java.exceptions;

public class EpicNotFoundException extends IllegalArgumentException {
    public EpicNotFoundException(int id) {
        super("Эпик с id " + id + " не найден");
    }
}
