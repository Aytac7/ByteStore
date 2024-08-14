package com.example.startapp.service.common;

import com.example.startapp.dto.request.common.AdRequest;
import com.example.startapp.dto.response.common.AdResponse;
import com.example.startapp.entity.*;
import com.example.startapp.enums.AdStatus;
import com.example.startapp.enums.PhonePrefix;
import com.example.startapp.exception.UserNotFoundException;
import com.example.startapp.mapper.AdMapper;
import com.example.startapp.repository.UserRepository;
import com.example.startapp.repository.common.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.startapp.enums.AdStatus.PENDING;

@Service
@RequiredArgsConstructor
public class AdService {
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ModelRepository modelRepository;
    private final FavoriteRepository favoriteRepository;
    private final AdMapper adMapper;



    public AdResponse createAd(AdRequest adRequest, List<MultipartFile> files) {

        if (adRequest.getCategoryId() == null ) {
            throw new IllegalArgumentException("Category ID must not be null or empty");
        }

        Category category=categoryRepository.findById(adRequest.getCategoryId()).orElseThrow(()-> new RuntimeException("Category not founds"));


        if (adRequest.getUserId() == null ) {
            throw new IllegalArgumentException("User ID must not be null or empty");
        }

        User user=userRepository.findById(adRequest.getUserId()).orElseThrow(()->new UserNotFoundException(HttpStatus.NOT_FOUND.name(), "user not found"));

        if (adRequest.getModelId() == null) {
            throw new IllegalArgumentException("Model ID must not be null or empty");
        }
        Model model = modelRepository.findById(adRequest.getModelId())
                .orElseThrow(() -> new RuntimeException("Model with ID " + adRequest.getModelId() + " not found"));

        if (adRequest.getBrandId() == null) {
            throw new IllegalArgumentException("Brand ID must not be null or empty");
        }
        Brand brand = brandRepository.findById(adRequest.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand with ID " + adRequest.getBrandId() + " not found"));

        Ad ad = adMapper.mapToEntity(adRequest);

        ad.setCategory(category);
        ad.setUser(user);
        ad.setModel(model);
        ad.setBrand(brand);

        List<Image> images = adMapper.mapMultipartFilesToImages(files, ad);
        ad.setImages(images);
        ad.setStatus(PENDING);

        Ad savedAd = adRepository.save(ad);

        return adMapper.mapToResponse(savedAd);
    }

//    public Ad createNewAd(AdRequest adRequest) {
//        if (adRequest.getImages() != null && adRequest.getImages().size() > 10) {
//            throw new IllegalArgumentException("You can upload a maximum of 10 images.");
//        }
//
//        Ad newAd = Ad.builder()
//                .category(adRequest.getCategory_id())
//                .brand(adRequest.getBrand_id())
//                .model(adRequest.getModel_id())
//                .price(adRequest.getPrice())
//                .header(adRequest.getHeader())
//                .isNew(adRequest.getIsNew())
//                .additionalInfo(adRequest.getAdditionalInfo())
//                .images(adRequest.getImages())
//                .status(AdStatus.PENDING)
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .phonePrefix(adRequest.getPhonePrefix())
//                .phoneNumber(adRequest.getPhoneNumber())
//                .build();
//
//        return adRepository.save(newAd);
//
//
//    }

    public Favorite addAdToFavorites(Integer userId, Long adId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ad ID"));

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setAd(ad);

        return favoriteRepository.save(favorite);
    }

}