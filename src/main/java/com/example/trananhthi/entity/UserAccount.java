package com.example.trananhthi.entity;

import com.example.trananhthi.common.BaseEntity;
import com.example.trananhthi.enumtype.Privacy;
import com.example.trananhthi.enumtype.Status;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.time.LocalDate;
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

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
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
    private LocalDate birthday;

    @Column(name = "gender")
    private String gender;

    @Column(name = "avatar",columnDefinition = "json")
    private String avatar;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "login_attempts")
    private Integer loginAttempts;

    @Enumerated(EnumType.STRING)
    @Column(name = "privacy_default")
    private Privacy privacyDefault;

    @Column(name = "name")
    private String name;
}
