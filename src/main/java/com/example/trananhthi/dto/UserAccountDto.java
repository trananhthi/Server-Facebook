package com.example.trananhthi.dto;

import com.example.trananhthi.enumtype.Privacy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    private String email;

    private String lastName;

    private String firstName;

    private String phone;

    private LocalDate birthday;

    private String gender;

    private String avatar;

    private LocalDateTime createdAt;

    private Privacy privacyDefault;

    public UserAccountDto(String id)
    {
        this.id = id;
    }
}
