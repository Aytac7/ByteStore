package com.example.startapp.dto.request;

import com.example.startapp.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserOtpRequest {


    private Integer fpid;

    private Integer otp;

    private Date expirationTime;

}