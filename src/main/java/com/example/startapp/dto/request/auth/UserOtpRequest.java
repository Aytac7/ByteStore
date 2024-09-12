package com.example.startapp.dto.request.auth;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserOtpRequest {


    private Long fpid;

    private Integer otp;

    private Date expirationTime;

}