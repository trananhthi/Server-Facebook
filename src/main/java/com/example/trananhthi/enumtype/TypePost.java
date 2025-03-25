package com.example.trananhthi.enumtype;

import lombok.Setter;

public enum TypePost implements EntityPropertyEnum<String> {
    /* text */
    TEXT("TEXT"),
    /* image */
    IMAGE("IMAGE"),
    /* video */
    VIDEO("VIDEO"),
    /* hybrid */
    HYBRID("HYBRID");

    @Setter
    private String value;

    TypePost(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
