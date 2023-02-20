package main.java.tasks;

import main.java.exceptions.StatusNotFound;

public enum Status {
    NEW("NEW"),
    IN_PROGRESS("IN_PROGRESS"),
    DONE("DONE");

    private final String statusToString;

    Status(String statusToString) {
        this.statusToString = statusToString;
    }

    public static Status getStatus(String statusToString) {
        for (Status status : Status.values()) {
            if (status.statusToString.equals(statusToString)) {
                return status;
            }
        }
        throw new StatusNotFound("Стаус " + statusToString + " не найден");
    }
}
