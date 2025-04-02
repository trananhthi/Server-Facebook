package com.example.trananhthi.model;

import com.example.trananhthi.enumtype.WSEvent;
import lombok.Data;

@Data
public class WSEventPayload<T> {
    private WSEvent event;
    private T data;
}
