package com.example.trananhthi.entity;

import com.example.trananhthi.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.time.Instant;

@Entity
@Table(name = "confirm_code")
@Getter
@Setter
public class ConfirmCode extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @OneToOne
    @JoinColumn(name = "user_account_id",referencedColumnName = "id")
    private UserAccount userAccount;

    @Column(nullable = false,unique = true)
    private String code;

    @Column(nullable = false)
    private Instant expiryDate;
}
