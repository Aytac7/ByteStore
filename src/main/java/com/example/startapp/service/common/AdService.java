package com.example.startapp.service.common;

import com.example.startapp.dto.request.common.AdRequest;
import com.example.startapp.entity.*;
import com.example.startapp.enums.AdStatus;
//import com.example.startapp.mapper.AdMapper;
import com.example.startapp.repository.UserRepository;
import com.example.startapp.repository.common.*;
import com.example.startapp.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j

public class AdService {
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ModelRepository modelRepository;
    private final S3Service s3Service;

    public void createAd(AdRequest adRequest, List<MultipartFile> files) {
        User user = userRepository.findById(adRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        Category category = categoryRepository.findById(adRequest.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

        Brand brand = brandRepository.findById(adRequest.getBrandId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid brand ID"));

        Model model = modelRepository.findById(adRequest.getModelId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid model ID"));

        Ad ad = Ad.builder()
                .category(category)
                .brand(brand)
                .model(model)
                .price(adRequest.getPrice())
                .header(adRequest.getHeader())
                .additionalInfo(adRequest.getAdditionalInfo())
                .isNew(adRequest.getIsNew())
                .user(user)
                .status(AdStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .phonePrefix(adRequest.getPhonePrefix())
                .phoneNumber(adRequest.getPhoneNumber())
                .build();

        List<Image> images = files.stream()
                .map(file -> {
                    String key = "ads/" + adRequest.getUserId() + "/" + file.getOriginalFilename();
                    String fileUrl = null;
                    try {
                        fileUrl = s3Service.uploadFile(key, file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    Image image = new Image();
                    image.setImageUrl(fileUrl);
                    image.setFileName(file.getOriginalFilename());
                    image.setFileType(file.getContentType());
                    image.setFilePath(fileUrl);
                    image.setAd(ad);

                    return image;
                })
                .collect(Collectors.toList());

        ad.setImages(images);

        adRepository.save(ad);
    }

//
//    return AdResponse.builder()
//            .id(savedAd.getId())
//            .price(savedAd.getPrice())
//            .header(savedAd.getHeader())
//            .additionalInfo(savedAd.getAdditionalInfo())
//            .isNew(savedAd.getIsNew())
//            .userId(savedAd.getUser().getUserId())
//            .categoryId(savedAd.getCategory().getId())
//            .brandId(savedAd.getBrand().getId())
//            .modelId(savedAd.getModel().getId())
//            .imageUrls(savedAd.getImages())
//            .phonePrefix(savedAd.getPhonePrefix())
//            .phoneNumber(savedAd.getPhoneNumber())
//            .status(savedAd.getStatus().name())
//            .build();
//}
}
