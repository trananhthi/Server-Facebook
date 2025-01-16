package com.example.trananhthi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@JsonIgnoreProperties(value = {"password","role","status","timeUpdated","loginAttempts","name"})
public class UserAccountDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String email;
    private String lastName;
    private String firstName;
    private String phone;
    private Date birthday;
    private String gender;
    private String avatar;
    private Date timeCreated;
    private String privacyDefault;
}
