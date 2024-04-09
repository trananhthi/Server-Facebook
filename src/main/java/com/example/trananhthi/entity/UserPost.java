package com.example.trananhthi.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "user_posts")
@Data
public class UserPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
