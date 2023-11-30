package com.example.trananhthi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@JsonIgnoreProperties(value = {"id","password","role","status","timeUpdated","loginAttempts"})
public class UserAccountDTO {
    private String email;
    private String lastName;
    private String firstName;
    private String phone;
    private Date birthday;
    private String gender;
    private AvatarDTO avatar;
    private Date timeCreated;
    private String privacyDefault;
}
