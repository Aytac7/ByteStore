package com.example.startapp.service.auth;

import com.amazonaws.services.s3.AmazonS3;
import com.example.startapp.dto.request.UserInfoRequest;
import com.example.startapp.dto.response.auth.UserDTO;
import com.example.startapp.dto.response.auth.UserDtoSpecific;
import com.example.startapp.dto.response.common.AdDTO;
import com.example.startapp.entity.common.Image;
import com.example.startapp.entity.auth.User;
import com.example.startapp.repository.auth.UserRepository;
import com.example.startapp.repository.common.ImageRepository;
import io.jsonwebtoken.Jwt;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final ImageRepository imageRepository;
    private final JwtService jwtService;
    private final AmazonS3 s3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;


    @Transactional
    public void updateInfo(String token, UserInfoRequest userInfoRequest, MultipartFile file) {
        Long userId = jwtService.extractUserId(token);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı id: " + userId));

        user.setName(userInfoRequest.getName());
        user.setSurname(userInfoRequest.getSurname());
        user.setPhonePrefix(userInfoRequest.getPhonePrefix());
        user.setPhoneNumber(userInfoRequest.getPhoneNumber());

        if (file != null && !file.isEmpty()) {
            String key = "profilephoto/" + userId + "/" + file.getOriginalFilename();
            String fileUrl;
            try {
                fileUrl = s3Service.uploadFile(key, file);
            } catch (IOException e) {
                throw new RuntimeException("Yeni profil şəkli yükləyəndə xəta yarandı. " + e.getMessage());
            }

            if (user.getProfilePhoto() != null) {
                Image existingProfilePhoto = user.getProfilePhoto();
                existingProfilePhoto.setFileName(file.getOriginalFilename());
                existingProfilePhoto.setFileType(file.getContentType());
                existingProfilePhoto.setFilePath(fileUrl);
                existingProfilePhoto.setImageUrl(fileUrl);

                imageRepository.save(existingProfilePhoto);
                System.out.println("Profil şəkli yükləndi");

            } else {
                Image newProfilePhoto = new Image();
                newProfilePhoto.setFileName(file.getOriginalFilename());
                newProfilePhoto.setFileType(file.getContentType());
                newProfilePhoto.setFilePath(fileUrl);
                newProfilePhoto.setImageUrl(fileUrl);

                newProfilePhoto.setUser(user);

                Image savedProfilePhoto = imageRepository.save(newProfilePhoto);
                System.out.println("Yeni profil şəkli saxlanıldı id: " + savedProfilePhoto.getId());
                user.setProfilePhoto(savedProfilePhoto);
            }

        } else {
            System.out.println("Heç bir fayl yüklənmədi");
        }
        userRepository.save(user);
        System.out.println("İstifadəçi məlumatları yeniləndi");
    }


    @Transactional
    public void deleteProfilePhoto(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı id: " + userId));

        if (user.getProfilePhoto() != null) {
            Image profilePhoto = user.getProfilePhoto();
            String imageKey = profilePhoto.getFilePath();
            s3Service.deleteFile(imageKey);
            imageRepository.delete(profilePhoto);
            user.setProfilePhoto(null);
            userRepository.save(user);


        } else {
            throw new RuntimeException("Profil şəkli yoxdu");
        }
    }


    public UserDtoSpecific getUserInfo(String token) {
        Long userId = jwtService.extractUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı"));

        Image profilePhoto = null;
        if (user.getProfilePhoto() != null) {
            String profilePhotoUrl = s3Client.getUrl(bucketName, user.getProfilePhoto().getFilePath()).toString();
            profilePhoto = Image.builder()
                    .imageUrl(profilePhotoUrl)
                    .build();
        }

        return UserDtoSpecific.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .username(user.getUsername())
                .email(user.getEmail())
                .phonePrefix(user.getPhonePrefix())
                .phoneNumber(user.getPhoneNumber())
                .profilePhoto(profilePhoto)
                .build();
    }

}