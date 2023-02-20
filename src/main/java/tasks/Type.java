package main.java.tasks;

import main.java.exceptions.TypeTaskNotFound;

public enum Type {
    SINGLE("SINGLE"),
    EPIC("EPIC"),
    SUB("SUB");

    private final String typeToString;

    Type(String typeToString) {
        this.typeToString = typeToString;
    }

    public static Type getType(String typeToString) {
        for (Type type : Type.values()) {
            if (type.typeToString.equals(typeToString)) {
                return type;
            }
        }
        throw new TypeTaskNotFound("Тип задачи " + typeToString + " не найден");
    }
}