package main.java.tasks;

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

        throw new IllegalArgumentException("Статус задачи \"" + statusToString + "\" не найден");
    }
}
