package com.example.trananhthi.common;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity implements Serializable {

    @Id
    @Column(
            name = "id",
            nullable = false
    )
    private String id = generateID();

    private String generateID() {
        return UUID.randomUUID().toString();
    }
}
