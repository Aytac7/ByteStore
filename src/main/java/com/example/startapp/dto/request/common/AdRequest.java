package com.example.startapp.dto.request.common;

import com.example.startapp.enums.AdStatus;
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
public class AdRequest {

    @NotNull
    private Long price;

    @NotNull
    private String header;

    @Size(max = 700)
    @NotNull
    private String additionalInfo;

    @NotNull
    private Boolean isNew;

    @NotNull
    private Integer userId;

    @NotNull
    private Long categoryId;

    @NotNull
    private Long brandId;

    @NotNull
    private Long modelId;

    @NotNull
    private AdStatus status;

    private List<MultipartFile> images;

    private String phoneNumber;
}
