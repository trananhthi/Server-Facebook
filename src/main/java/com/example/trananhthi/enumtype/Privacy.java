package com.example.trananhthi.enumtype;

import lombok.Setter;

public enum Privacy implements EntityPropertyEnum<String> {
    /* Chỉ bạn bè */
    FRIEND("FRIEND"),
    /* Công khai */
    PUBLIC("PUBLIC"),
    /* Tùy chỉnh */
    CUSTOM("CUSTOM"),
    /* Ngoại trừ bạn bè */
    EXCEPT_FRIEND("EXCEPT_FRIEND"),
    /* Chỉ bạn bè cụ thể */
    SPECIFIC_FRIEND("SPECIFIC_FRIEND"),
    /* Chỉ mình tôi */
    ONLY_ME("ONLY_ME");

    @Setter
    private String value;

    Privacy(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
