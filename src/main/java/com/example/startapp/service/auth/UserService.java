package com.example.startapp.service.auth;

import com.amazonaws.services.s3.AmazonS3;
import com.example.startapp.dto.request.UserInfoRequest;
import com.example.startapp.dto.response.auth.UserDTO;
import com.example.startapp.dto.response.auth.UserDtoSpecific;
import com.example.startapp.dto.response.common.AdDTO;
import com.example.startapp.dto.response.common.ImageDto;
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
        User user = userRepository.findById(userId).orElseThrow(() ->
                new RuntimeException("User not found with id: " + userId));
        if (userInfoRequest != null) {
            if (userInfoRequest.getName() != null) {
                System.out.println("Updating name: " + userInfoRequest.getName());
                user.setName(userInfoRequest.getName());
            }
            if (userInfoRequest.getSurname() != null) {
                System.out.println("Updating surname: " + userInfoRequest.getSurname());
                user.setSurname(userInfoRequest.getSurname());
            }
            if (userInfoRequest.getPhonePrefix() != null) {
                System.out.println("Updating phone prefix: " + userInfoRequest.getPhonePrefix());
                user.setPhonePrefix(userInfoRequest.getPhonePrefix());
            }
            if (userInfoRequest.getPhoneNumber() != null) {
                System.out.println("Updating phone number: " + userInfoRequest.getPhoneNumber());
                user.setPhoneNumber(userInfoRequest.getPhoneNumber());
            }
            if (file != null && !file.isEmpty()) {
                String key = "profilephoto/" + userId + "/" + file.getOriginalFilename();
                String fileUrl;
                try {
                    fileUrl = s3Service.uploadFile(key, file);
                } catch (IOException e) {
                    throw new RuntimeException("Error uploading new profile photo. " + e.getMessage());
                }

                if (user.getProfilePhoto() != null) {
                    Image existingProfilePhoto = user.getProfilePhoto();
                    existingProfilePhoto.setFileName(file.getOriginalFilename());
                    existingProfilePhoto.setFileType(file.getContentType());
                    existingProfilePhoto.setFilePath(fileUrl);
                    existingProfilePhoto.setImageUrl(fileUrl);
                    imageRepository.save(existingProfilePhoto);
                } else {
                    Image newProfilePhoto = new Image();
                    newProfilePhoto.setFileName(file.getOriginalFilename());
                    newProfilePhoto.setFileType(file.getContentType());
                    newProfilePhoto.setFilePath(fileUrl);
                    newProfilePhoto.setImageUrl(fileUrl);
                    newProfilePhoto.setUser(user);

                    Image savedProfilePhoto = imageRepository.save(newProfilePhoto);
                    user.setProfilePhoto(savedProfilePhoto);
                }
            }

            userRepository.save(user);
        }


//    @Transactional
//    public void deleteProfilePhoto(String token) {
//        Long userId = jwtService.extractUserId(token);
//        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı id: " + userId));
//
//        if (user.getProfilePhoto() != null) {
//            Image profilePhoto = user.getProfilePhoto();
//            // Extract relative key
//            String imageKey = profilePhoto.getFilePath().replace("https://bytestore-bucket.s3.amazonaws.com/", "");
//
//            try {
//                s3Service.deleteFile(imageKey); // Attempt to delete from S3
//                imageRepository.delete(profilePhoto); // Delete record from DB
//                user.setProfilePhoto(null);
//                userRepository.save(user);
//            } catch (RuntimeException e) {
//                throw new RuntimeException("Profil şəklini silmək mümkün olmadı. Səbəb: " + e.getMessage(), e);
//            }
//        } else {
//            throw new RuntimeException("Profil şəkli mövcud deyil");
//        }
//
//    }
    }
        @Transactional
        public void deleteProfilePhoto (String token){
            Long userId = jwtService.extractUserId(token);
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı id: " + userId));

            if (user.getProfilePhoto() != null) {
                Image profilePhoto = user.getProfilePhoto();
                String imageKey = profilePhoto.getFilePath();
                try {
                    s3Service.deleteFile(imageKey);
                } catch (Exception e) {
                    throw new RuntimeException("Profil şəklini silmək mümkün olmadı. Səbəb: " + e.getMessage());
                }
                imageRepository.delete(profilePhoto);
                user.setProfilePhoto(null);
                userRepository.save(user);
                System.out.println("Profil şəkli silindi");
            } else {
                throw new RuntimeException("Profil şəkli mövcud deyil");
            }
        }


    public UserDtoSpecific getUserInfo(String token) {
        Long userId = jwtService.extractUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ImageDto profilePhoto = null;
        if (user.getProfilePhoto() != null) {
            profilePhoto = ImageDto.builder()
                    .imageUrl(user.getProfilePhoto().getImageUrl())
                    .build();
        }
        return UserDtoSpecific.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .username(user.getUsername())
                .phonePrefix(user.getPhonePrefix())
                .phoneNumber(user.getPhoneNumber())
                .profilePhoto(profilePhoto)
                .role(user.getRole())
                .build();
    }
}