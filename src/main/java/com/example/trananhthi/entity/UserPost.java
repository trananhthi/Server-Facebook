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
@Table(name = "user_posts")
@Setter
@Getter
public class UserPost extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id",referencedColumnName = "id")
    private UserAccount author;

    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    private String typePost;

    @OneToOne
    @JoinColumn(name = "parent_post",referencedColumnName = "id")
    private UserPost parentPost;

    private Integer view;

    private String privacy;

    private String tag;

    private String hashtag;

    private Integer priority;

    private String status;
}
