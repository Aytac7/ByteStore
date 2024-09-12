package com.example.startapp.controller.common;

import com.example.startapp.dto.request.UserInfoRequest;
import com.example.startapp.dto.request.common.AdRequest;
import com.example.startapp.entity.auth.User;
import com.example.startapp.service.auth.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@AuthenticationPrincipal User user, @RequestParam("userInfoRequest") String userInfoRequestJson, @RequestParam("file") MultipartFile file) {
        try {
            UserInfoRequest userInfoRequest = objectMapper.readValue(userInfoRequestJson, UserInfoRequest.class);
            userService.updateInfo(user.getUserId(), userInfoRequest, file);
            return ResponseEntity.status(HttpStatus.OK).body("Profil məlumatları uğurla yeniləndi");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Məlumatlarda xəta: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Daxili error: " + e.getMessage());
        }

    }


    @PutMapping("/delete")
    public ResponseEntity<String> deletePP(@AuthenticationPrincipal User user) {
        try {
            userService.deleteProfilePhoto(user.getUserId());
            return ResponseEntity.status(HttpStatus.OK).body("Profil şəkli uğurla silindi");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Profil şəklini silərkən xəta yarandı " + e.getMessage());
        }
    }
}