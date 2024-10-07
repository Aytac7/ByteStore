package com.example.startapp.dto.response.auth;

import com.example.startapp.entity.common.Image;
import com.example.startapp.enums.PhonePrefix;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long userId;

    private String name;

    private String surname;

    private String username;

    private String email;

    PhonePrefix phonePrefix;

    String phoneNumber;

    private Image profilePhoto;

}
