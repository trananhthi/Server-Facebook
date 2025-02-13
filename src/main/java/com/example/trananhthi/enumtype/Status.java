package com.example.trananhthi.enumtype;

import lombok.Setter;

public enum Status implements EntityPropertyEnum<String> {
    /* Active */
    ACT("ACT"),
    /* Delete */
    DEL("DEL"),
    /* Pending */
    PEN("PEN"),
    /* Temporary */
    TEM("TEM");

    @Setter
    private String value;

    Status(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public static synchronized String stringToStatus(String e) {
        if (e.isEmpty()) return null;
        return switch (e) {
            case "ACT" -> Status.ACT.getValue();
            case "DEL" -> Status.DEL.getValue();
            case "PEN" -> Status.PEN.getValue();
            case "TEMP" -> Status.TEM.getValue();
            default -> null;
        };
    }
}
