package com.example.trananhthi.entity;

import com.example.trananhthi.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.util.Date;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "comments")
@Setter
@Getter
public class Comment extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "post_id",referencedColumnName = "id")
    private UserPost userPost;
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private UserAccount userAccount;
    private String content;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    private String status;
}
