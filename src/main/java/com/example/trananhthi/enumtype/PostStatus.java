package com.example.trananhthi.enumtype;

import lombok.Setter;

public enum PostStatus implements EntityPropertyEnum<String>{
    /* PUBLISHED */
    PUBLISHED("PUBLISHED"),
    /* Delete */
    DEL("DEL"),
    /* Pending */
    PEN("PEN"),
    /* DRAFT */
    DRAFT("DRAFT"),
    /* ARCHIVED */
    ARCHIVED("ARCHIVED");

    @Setter
    private String value;

    PostStatus(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
