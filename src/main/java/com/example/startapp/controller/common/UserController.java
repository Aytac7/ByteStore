package com.example.startapp.controller.common;

import com.example.startapp.dto.request.UserInfoRequest;
import com.example.startapp.dto.response.auth.UserDtoSpecific;
import com.example.startapp.exception.UserNotFoundException;
import com.example.startapp.service.auth.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final ObjectMapper objectMapper;
    private final UserService userService;


    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            UserDtoSpecific userInfo = userService.getUserInfo(token);
            return ResponseEntity.ok(userInfo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UserNotFoundException("İstifadəçi tapılmadı", e.getMessage()));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("userInfoRequest") String userInfoRequestJson, @RequestParam("file") MultipartFile file) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            UserInfoRequest userInfoRequest = objectMapper.readValue(userInfoRequestJson, UserInfoRequest.class);
            userService.updateInfo(token, userInfoRequest, file);
            return ResponseEntity.status(HttpStatus.OK).body("Profil məlumatları uğurla yeniləndi");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Məlumatlarda xəta: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Daxili error: " + e.getMessage());
        }

    }


    @PutMapping("/delete/{userId}")
    public ResponseEntity<String> deletePP(@PathVariable Long userId) {
        try {
            userService.deleteProfilePhoto(userId);
            return ResponseEntity.status(HttpStatus.OK).body("Profil şəkli uğurla silindi");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Profil şəklini silərkən xəta yarandı " + e.getMessage());
        }
    }
}