package com.example.startapp.dto.request;

import com.example.startapp.enums.PhonePrefix;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoRequest {

    @NotNull
    private String name;

    @NotNull
    private String surname;

    private PhonePrefix phonePrefix;

    private String phoneNumber;

    @NotNull
    private Long userId;

    @Size(max = 1)
    private List<MultipartFile> image;
}
