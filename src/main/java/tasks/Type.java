package main.java.tasks;

public enum Type {
    SINGLE("SINGLE"),
    EPIC("EPIC"),
    SUB("SUB");

    private final String value;

    Type(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Type valueOfOrTrow(String value) {
        for (Type type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Неизвестный тип задачи: " + value);
    }
}