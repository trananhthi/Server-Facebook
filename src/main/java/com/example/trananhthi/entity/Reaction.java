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
@Table(name = "reactions")
@Getter
@Setter
public class Reaction extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "post_id")
    private String postId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "type_reaction")
    private String typeReaction;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
}
