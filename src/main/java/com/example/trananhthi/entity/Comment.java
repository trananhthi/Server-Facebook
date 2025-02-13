package com.example.trananhthi.entity;

import com.example.trananhthi.common.BaseEntity;
import com.example.trananhthi.enumtype.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "comments")
@Setter
@Getter
@NoArgsConstructor
public class Comment extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "post_id")
    private String postId;

    @JoinColumn(name = "user_id")
    private String userId;

    @Column(name = "content")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
}
