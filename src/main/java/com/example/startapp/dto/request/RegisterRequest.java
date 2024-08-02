package com.example.startapp.dto.request;

import com.example.startapp.enums.PhonePrefix;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
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

    @NotBlank(message = "The phone prefix field can't be blank")
    private String phonePrefix;

    @NotBlank(message = "The phoneNumber field can't be blank")
    @Pattern(regexp = "^\\d{7}$",
            message = "Phone number must be 7 numbers")
    private String phoneNumber;

}