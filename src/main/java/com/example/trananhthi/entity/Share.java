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
@Table(name = "shares")
@Getter
@Setter
public class Share extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "post_id",referencedColumnName = "id")
    private UserPost userPost;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private UserAccount userAccount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
}
