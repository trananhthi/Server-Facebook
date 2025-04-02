package com.example.trananhthi.enumtype;

import lombok.Setter;

public enum WSEvent implements EntityPropertyEnum<String> {
    /* Send message */
    SEND_MESSAGE("SEND_MESSAGE"),
    /* Delete message */
    DELETE_MESSAGE("DELETE_MESSAGE"),
    /* Update message */
    UPDATE_MESSAGE("UPDATE_MESSAGE"),
    /* React to message */
    REACTION_MESSAGE("REACTION_MESSAGE"),
    /* Mark message as read */
    READ_MESSAGE("READ_MESSAGE"),
    /* Typing indicator */
    TYPING("TYPING"),
    /* Stop typing indicator */
    STOP_TYPING("STOP_TYPING");

    @Setter
    private String value;

    WSEvent(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
