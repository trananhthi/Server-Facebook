package com.example.trananhthi.entity;

import com.example.trananhthi.common.BaseEntity;
import com.example.trananhthi.enumtype.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "post_images")
@Getter
@Setter
public class PostImage extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "post_id")
    private String postId;

    @Column(name = "url")
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
}
