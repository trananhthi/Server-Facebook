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
@Table(name = "post_images")
@Getter
@Setter
public class PostImage extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "post_id",referencedColumnName = "id")
    private UserPost userPost;

    @Column(name = "url")
    private String url;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
}
