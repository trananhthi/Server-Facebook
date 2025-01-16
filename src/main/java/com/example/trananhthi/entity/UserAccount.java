package com.example.trananhthi.entity;

import com.example.trananhthi.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.util.Date;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "user_accounts")
@Getter
@Setter
public class UserAccount extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "role")
    private String role;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "birthday")
    @Temporal(TemporalType.DATE)
    private Date birthday;

    @Column(name = "gender")
    private String gender;

    @Column(name = "avatar",columnDefinition = "json")
    private String avatar;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeCreated;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeUpdated;

    @Column(name = "login_attempts")
    private Integer loginAttempts;

    @Column(name = "privacy_default")
    private String privacyDefault;

    @Column(name = "name")
    private String name;
}
