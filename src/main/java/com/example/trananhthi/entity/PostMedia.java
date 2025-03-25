package com.example.trananhthi.entity;

import com.example.trananhthi.common.BaseEntity;
import com.example.trananhthi.enumtype.MediaType;
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
@Table(name = "post_media")
@Getter
@Setter
public class PostMedia extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "post_id")
    private String postId;

    @Column(name = "url")
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private MediaType type;

    @Column(name = "size")
    private Integer size;

    @Column(name = "visual_index")
    private Integer visualIndex;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

}
