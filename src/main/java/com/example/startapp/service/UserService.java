package com.example.startapp.service;

import com.example.startapp.dto.request.FileRequestDto;
import com.example.startapp.entity.User;
import com.example.startapp.exception.EmptyFileException;
import com.example.startapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
//    private final UserRepository userRepository;
//
//
//    public Integer updateUser(User model) {
//        User user =  User.builder()
//                .name(model.getName())
//                .phonePrefix(model.getPhonePrefix())
//                .phoneNumber(model.getPhoneNumber())
//                .build();
//        userRepository.save(user);
//        return user.getUserId();
//    }
//
//    public ResponseEntity<String> updatePhoto(MultipartFile file, Principal principal) {
//        String username = principal.getName();
//        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//        Path userDirectory = Paths.get(uploadDir).resolve(username).normalize();
//        Path destinationPath = userDirectory.resolve(fileName).normalize();
//
//        try {
//            if (file.isEmpty()) {
//                return new ResponseEntity<>("Empty file", HttpStatus.BAD_REQUEST);
//            }
//
//            Files.createDirectories(userDirectory);
//            Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
//
//        } catch (IOException e) {
//            return new ResponseEntity<>("Store exception: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//        return new ResponseEntity<>(destinationPath.toString(), HttpStatus.OK);
//    }
//
//
//}

}