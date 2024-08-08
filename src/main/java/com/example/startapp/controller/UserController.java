package com.example.startapp.controller;

import com.example.startapp.entity.User;
import com.example.startapp.service.AuthService;
import com.example.startapp.service.CustomOAuth2UserService;
import com.example.startapp.service.RefreshTokenService;
import com.example.startapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
   // private final UserService userService;
//
//    @PostMapping("/photoUpload")
//    public ResponseEntity<Map<String, String>> handleFileUploadUsingCurl(
//            @RequestParam("file") MultipartFile file) throws IOException {
//
//        Map<String, String> map = new HashMap<>();
//
//        map.put("fileName", file.getOriginalFilename());
//        map.put("fileContentType", file.getContentType());
//
//        map.put("message", "File upload done");
//        return ResponseEntity.ok(map);
//    }
}

