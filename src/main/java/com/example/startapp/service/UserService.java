package com.example.startapp.service;

import com.example.startapp.entity.User;
import com.example.startapp.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
//    private final UserRepository userRepository;
//
//    public Long findById(@NotNull Long id) {
//        Optional<User> user = userRepository.findById(id);
//        return user.get().getUserId();
//    }
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