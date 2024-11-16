package com.example.startapp.controller.auth;


import com.example.startapp.dto.response.auth.ChangePassword;
import com.example.startapp.service.auth.ForgotPasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/forgotPassword")

public class ForgotPasswordController {


    private final ForgotPasswordService forgotPasswordService;


    public ForgotPasswordController(ForgotPasswordService forgotPasswordService) {
        this.forgotPasswordService=forgotPasswordService;
    }


    @PostMapping("/verifyMail/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email) {
        return  forgotPasswordService.verifyEmail(email);


    }
    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp, @PathVariable String email) throws IOException {
            return forgotPasswordService.verifyOtp(otp, email);

    }

    @PostMapping("/changePassword/{token}")
    public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePassword changePassword,
                                                        @PathVariable String token) {
       return forgotPasswordService.changePasswordHandler(changePassword,token);
    }

}