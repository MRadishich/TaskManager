package main.java.tasks;

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

        throw new IllegalArgumentException("Тип задачи \"" + typeToString + "\" не найден");
    }
}