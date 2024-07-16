package com.example.startapp.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {

    private String name;
    private String surname;
    private String email;
    private String username;
    private String password;
    private String repeatPassword;
    private String phoneNumber;

}