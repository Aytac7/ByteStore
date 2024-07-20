package com.example.startapp.dto.request;

import com.example.startapp.entity.ForgotPassword;
import com.example.startapp.entity.RefreshToken;
import com.example.startapp.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank(message = "The name field can't be blank")
    private String name;

    @NotBlank(message = "The username field can't be blank")
    private String username;

    @NotBlank(message = "The surname field can't be blank")
    private String surname;

    @NotBlank(message = "The email field can't be blank")
    @Email(message = "Please enter email in proper format!")
    private String email;

    @NotBlank(message = "The password field can't be blank")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{7,}$",
            message = "Password must be at least 7 characters long, contain at least one uppercase letter, one lowercase letter, and one number.")
    private String password;

    @NotBlank(message = "The password field can't be blank")
    private String repeatPassword;


    @NotBlank(message = "The phoneNumber field can't be blank")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$",
            message = "Phone number must be between 10 and 15 digits, optionally starting with '+'. format=+994551212333")
    private String phoneNumber;

}