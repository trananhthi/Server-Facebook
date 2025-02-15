package com.example.trananhthi.enumtype;

import lombok.Setter;

public enum MediaType implements EntityPropertyEnum<String> {
    /* image */
    IMAGE("IMAGE"),
    /* video */
    VIDEO("VIDEO");

    @Setter
    private String value;

    MediaType(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
