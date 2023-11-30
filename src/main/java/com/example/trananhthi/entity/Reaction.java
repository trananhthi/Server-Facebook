package com.example.trananhthi.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "reactions")
@Data
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "post_id",referencedColumnName = "id")
    private UserPost userPost;
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private UserAccount userAccount;
    private String typeReaction;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    private String status;
}
