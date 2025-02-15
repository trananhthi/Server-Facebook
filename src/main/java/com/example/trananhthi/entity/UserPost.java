package com.example.trananhthi.entity;

import com.example.trananhthi.common.BaseEntity;
import com.example.trananhthi.enumtype.PostStatus;
import com.example.trananhthi.enumtype.Privacy;
import com.example.trananhthi.enumtype.TypePost;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;

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

    @Column(name = "content")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_post")
    private TypePost typePost;

    @OneToOne
    @JoinColumn(name = "parent_post",referencedColumnName = "id")
    private UserPost parentPost;

    @Column(name = "view")
    private Integer view;

    @Enumerated(EnumType.STRING)
    @Column(name = "privacy")
    private Privacy privacy;

    @Column(name = "tag")
    private String tag;

    @Column(name = "hashtag")
    private String hashtag;

    @Column(name = "priority")
    private Integer priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PostStatus status;
}
