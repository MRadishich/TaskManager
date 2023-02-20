package main.java.exceptions;

public class StatusNotFound extends RuntimeException {
    public StatusNotFound(String message) {
        super(message);
    }
}
