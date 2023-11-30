package com.example.trananhthi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "comments")
@Setter
@Getter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
