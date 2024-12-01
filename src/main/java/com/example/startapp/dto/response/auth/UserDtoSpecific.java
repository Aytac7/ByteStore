package com.example.startapp.dto.response.auth;

import com.example.startapp.entity.common.Image;
import com.example.startapp.enums.PhonePrefix;
import com.example.startapp.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor


public class UserDtoSpecific {

    private String name;

    private String surname;

    private String username;

    private String email;

    private PhonePrefix phonePrefix;

    private String phoneNumber;

    private Image profilePhoto;

    private UserRole role;

}
