package com.example.startapp.service.common;

import com.example.startapp.dto.request.common.AdCriteriaRequest;
import com.example.startapp.dto.request.common.AdRequest;
import com.example.startapp.dto.response.common.*;
import com.example.startapp.entity.auth.User;
import com.example.startapp.entity.common.*;
import com.example.startapp.enums.AdStatus;
import com.example.startapp.enums.PhonePrefix;
import com.example.startapp.exception.AdNotFoundException;
import com.example.startapp.repository.auth.UserRepository;
import com.example.startapp.repository.common.*;
import com.example.startapp.service.auth.S3Service;
import com.example.startapp.service.specification.AdSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
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
    private final FavoriteRepository favoriteRepository;



    public Map<String, Object> getAdsWithFilter(Long userId, AdCriteriaRequest adCriteriaRequest, Pageable pageable){
        Specification<Ad> specification = AdSpecification.getAdByCriteriaRequest(adCriteriaRequest);
        Page<Ad> ads=adRepository.findAll(specification,pageable);

        Set<Long> favoriteAdIds;
        if(userId!=null){
            List<Favorite> favorites= favoriteRepository.findByUserUserId(userId);
            favoriteAdIds = favorites.stream()
                    .map(fav -> fav.getAd().getId())
                    .collect(Collectors.toSet());
        } else {
            favoriteAdIds = new HashSet<>();
        }


        Page<AdDTOSpecific> adDTOSpecificPage = ads.map(ad -> AdDTOSpecific.builder()
                .id(ad.getId())
                .categoryId(ad.getCategory().getId())
                .modelId(ad.getModel().getId())
                .modelName(ad.getModel().getName())
                .categoryName(ad.getCategory().getName())
                .price(ad.getPrice())
                .header(ad.getHeader())
                .createdAt(ad.getCreatedAt())
                .isFavorite(userId != null && favoriteAdIds.contains(ad.getId()))
                .imageUrls(ad.getImages().stream()
                        .map(Image::getImageUrl)
                        .collect(Collectors.toList()))
                .build());

        Map<String, Object> response = new HashMap<>();
        response.put("totalCount", ads.getTotalElements());
        response.put("page", adDTOSpecificPage);
        return response;

    }

    public Map<String,Object> getAllNewAds(Long userId,Pageable pageable) {
        Page<Ad> ads = adRepository.findByIsNewTrueAndStatus(pageable, AdStatus.APPROVED);

        getFavoriteAdIds(userId);

        Page<AdDTOSpecific> adDTOSpecificPage = ads.map(ad -> AdDTOSpecific.builder()
                .id(ad.getId())
                .categoryId(ad.getCategory().getId())
                .modelId(ad.getModel().getId())
                .modelName(ad.getModel().getName())
                .categoryName(ad.getCategory().getName())
                .price(ad.getPrice())
                .header(ad.getHeader())
                .createdAt(ad.getCreatedAt())
                .isFavorite(userId != null && getFavoriteAdIds(userId).contains(ad.getId()))
                .imageUrls(ad.getImages().stream()
                        .map(Image::getImageUrl)
                        .collect(Collectors.toList()))
                .build());
        Map<String,Object> response= new  HashMap<>();
        response.put("totalCount", ads.getTotalElements());
        response.put("page", adDTOSpecificPage);
        return response;
    }

        public Map<String,Object> getAllSecondHandAds(Long userId,Pageable pageable) {
        Page<Ad> ads = adRepository.findByIsNewFalseAndStatus(pageable, AdStatus.APPROVED);

            getFavoriteAdIds(userId);
         Page<AdDTOSpecific>adDTOSpecificPage= ads.map(ad -> AdDTOSpecific.builder()
                .id(ad.getId())
                .categoryId(ad.getCategory().getId())
                .modelId(ad.getModel().getId())
                .modelName(ad.getModel().getName())
                .categoryName(ad.getCategory().getName())
                .price(ad.getPrice())
                .header(ad.getHeader())
                .createdAt(ad.getCreatedAt())
                .isFavorite(userId != null && getFavoriteAdIds(userId).contains(ad.getId()))
                .imageUrls(ad.getImages().stream()
                        .map(Image::getImageUrl)
                        .collect(Collectors.toList()))
                .build());
            Map<String,Object> response= new  HashMap<>();
            response.put("totalCount", ads.getTotalElements());
            response.put("page", adDTOSpecificPage);
            return response;
    }

    private Set<Long> getFavoriteAdIds(Long userId) {
        if (userId != null) {
            List<Favorite> favorites = favoriteRepository.findByUserUserId(userId);
            return favorites.stream()
                    .map(favorite -> favorite.getAd().getId())
                    .collect(Collectors.toSet());
        } else {
            return new HashSet<>();
        }
    }


    public List<AdDTO> getAdsByModel(Long modelId) {
        List<Ad> ads = adRepository.findByModelIdAndStatus(modelId, AdStatus.APPROVED);

        return ads.stream().map(ad -> AdDTO.builder()
                .id(ad.getId())
                .categoryId(ad.getCategory().getId())
                .brandId(ad.getBrand().getId())
                .modelId(ad.getModel().getId())
                .price(ad.getPrice())
                .header(ad.getHeader())
                .additionalInfo(ad.getAdditionalInfo())
                .isNew(ad.getIsNew())
                .city(ad.getCity())
                .imageUrls(ad.getImages().stream()
                        .map(Image::getImageUrl)
                        .collect(Collectors.toList()))
                .userId(ad.getUser().getUserId())
                .phonePrefix(ad.getPhonePrefix())
                .phoneNumber(ad.getPhoneNumber())
                .createdAt(ad.getCreatedAt())

                .build()
        ).collect(Collectors.toList());
    }







    public void createAd(AdRequest adRequest, List<MultipartFile> files) {
        User user = userRepository.findById(adRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        Category category = categoryRepository.findById(adRequest.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

        Brand brand = brandRepository.findById(adRequest.getBrandId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid brand ID"));

        Model model = modelRepository.findById(adRequest.getModelId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid model ID"));
        PhonePrefix phonePrefix;
        String phoneNumber;
        String name;
        String surname;
        if (user.getPhonePrefix() != null && user.getPhoneNumber() != null) {
            phonePrefix = user.getPhonePrefix();
            phoneNumber = user.getPhoneNumber();
        } else {
            phonePrefix = adRequest.getPhonePrefix();
            phoneNumber = adRequest.getPhoneNumber();
        }

        if (user.getName() != null && user.getSurname() != null) {
            name = user.getName();
            surname = user.getSurname();
        } else {
            name = adRequest.getName();
            surname = adRequest.getSurname();
        }

        Ad ad = Ad.builder()
                .category(category)
                .brand(brand)
                .model(model)
                .price(adRequest.getPrice())
                .header(adRequest.getHeader())
                .additionalInfo(adRequest.getAdditionalInfo())
                .isNew(adRequest.getIsNew())
                .user(user)
                .city(adRequest.getCity())
                .status(AdStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .phonePrefix(phonePrefix)
                .phoneNumber(phoneNumber)
                .name(name)
                .surname(surname)
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

//    public List<AdDTO> getAllAds() {
//        return adRepository.findAll().stream().map(
//                ad -> AdDTO.builder()
//                        .id(ad.getId())
//                        .categoryId(ad.getCategory().getId())
//                        .brandId(ad.getBrand().getId())
//                        .modelId(ad.getModel().getId())
//                        .price(ad.getPrice())
//                        .header(ad.getHeader())
//                        .additionalInfo(ad.getAdditionalInfo())
//                        .city(ad.getCity())
//                        .isNew(ad.getIsNew())
//                        .imageUrls(ad.getImages().stream()
//                                .map(Image::getImageUrl)
//                                .collect(Collectors.toList()))
//                        .userId(ad.getUser().getUserId())
//                        .phonePrefix(ad.getPhonePrefix())
//                        .phoneNumber(ad.getPhoneNumber())
//                        .status(ad.getStatus().toString())
//                        .build()
//        ).collect(Collectors.toList());
//    }










    public List<AdDTO> getAdsByUserId(Long userId) {
        List<Ad> ads = adRepository.findByUser_UserId(userId);


        return ads.stream().map(ad -> AdDTO.builder()
                .id(ad.getId())
                .userId(ad.getUser().getUserId())
                .price(ad.getPrice())
                .header(ad.getHeader())
                .additionalInfo(ad.getAdditionalInfo())
                .isNew(ad.getIsNew())
                .categoryId(ad.getCategory().getId())
                .brandId(ad.getBrand().getId())
                .modelId(ad.getModel().getId())
                .modelName(ad.getModel().getName())
                .categoryName(ad.getCategory().getName())
                .brandName(ad.getBrand().getName())
                .imageUrls(ad.getImages().stream()
                        .map(Image::getImageUrl)
                        .collect(Collectors.toList()))
                .phonePrefix(ad.getPhonePrefix())
                .phoneNumber(ad.getPhoneNumber())
                .status(ad.getStatus().toString())
                .city(ad.getCity())
                .build()
        ).collect(Collectors.toList());
    }

    public AdDTO getAdById(Long id) {
        Ad ad = adRepository.findById(id).orElseThrow(() -> new AdNotFoundException("Invalid ad ID"));
        return AdDTO.builder()
                .id(ad.getId())
                .userId(ad.getUser().getUserId())
                .header(ad.getHeader())
                .price(ad.getPrice())
                .isNew(ad.getIsNew())
                .additionalInfo(ad.getAdditionalInfo())
                .phonePrefix(ad.getPhonePrefix())
                .phoneNumber(ad.getPhoneNumber())
                .categoryId(ad.getCategory().getId())
                .brandId(ad.getBrand().getId())
                .modelId(ad.getModel().getId())
                .modelName(ad.getModel().getName())
                .categoryName(ad.getCategory().getName())
                .brandName(ad.getBrand().getName())
                .city(ad.getCity())
                .imageUrls(ad.getImages().stream()
                        .map(Image::getImageUrl)
                        .collect(Collectors.toList()))
                .status(ad.getStatus().toString())
                .build();

    }

    public List<AdDTO> getUserAdsByStatus(Long userId, AdStatus status) {
        List<Ad> ads = adRepository.findByUser_UserIdAndStatus(userId, status);
        return ads.stream()
                .map(this::convertToAdDTO)
                .collect(Collectors.toList());
    }

    private AdDTO convertToAdDTO(Ad ad) {
        return AdDTO.builder()
                .id(ad.getId())
                .price(ad.getPrice())
                .header(ad.getHeader())
                .additionalInfo(ad.getAdditionalInfo())
                .isNew(ad.getIsNew())
                .userId(ad.getUser().getUserId())
                .categoryId(ad.getCategory().getId())
                .brandId(ad.getBrand().getId())
                .modelId(ad.getModel().getId())
                .modelName(ad.getModel().getName())
                .categoryName(ad.getCategory().getName())
                .brandName(ad.getBrand().getName())
                .city(ad.getCity())
                .imageUrls(ad.getImages().stream()
                        .map(Image::getImageUrl)
                        .collect(Collectors.toList()))
                .phonePrefix(ad.getPhonePrefix())
                .phoneNumber(ad.getPhoneNumber())
                .status(ad.getStatus().toString())
                .build();
    }


    public void updateAd(Long adId, AdRequest adRequest, List<MultipartFile> files) {
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new AdNotFoundException("Invalid ad ID"));

        Category category = categoryRepository.findById(adRequest.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

        Brand brand = brandRepository.findById(adRequest.getBrandId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid brand ID"));

        Model model = modelRepository.findById(adRequest.getModelId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid model ID"));


        if (!ad.getStatus().equals(AdStatus.PENDING) && !ad.getStatus().equals(AdStatus.REJECTED)) {
            throw new IllegalStateException("Only pending or rejected ads can be edited");
        }

        ad.setCategory(category);
        ad.setBrand(brand);
        ad.setModel(model);
        ad.setHeader(adRequest.getHeader());
        ad.setAdditionalInfo(adRequest.getAdditionalInfo());
        ad.setPrice(adRequest.getPrice());
        ad.setPhonePrefix(adRequest.getPhonePrefix());
        ad.setPhoneNumber(adRequest.getPhoneNumber());
        ad.setUpdatedAt(LocalDateTime.now());
        ad.setIsNew(adRequest.getIsNew());
        ad.setName(adRequest.getName());
        ad.setSurname(adRequest.getSurname());
        ad.setCity(adRequest.getCity());

        if (files != null && !files.isEmpty()) {
            List<Image> images = files.stream()
                    .map(file -> {
                        String key = "ads/" + adRequest.getUserId() + "/" + file.getOriginalFilename();
                        String fileUrl;
                        try {
                            fileUrl = s3Service.uploadFile(key, file);
                        } catch (IOException e) {
                            throw new RuntimeException("Error uploading file: " + file.getOriginalFilename(), e);
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
        }

        ad.setStatus(AdStatus.PENDING);

        adRepository.save(ad);
    }

    public void deleteAdById(Long id) {
        adRepository.deleteById(id);
    }

    //    public Page<AdDTOSpecific> getAllSecondHandAds(Pageable pageable) {
//        Page<Ad> adPage = adRepository.findByIsNewFalseAndStatus(pageable, AdStatus.APPROVED);
//        return adPage.map(ad -> AdDTOSpecific.builder()
//                .id(ad.getId())
//                .categoryId(ad.getCategory().getId())
//                .modelId(ad.getModel().getId())
//                .price(ad.getPrice())
//                .header(ad.getHeader())
//                .createdAt(ad.getCreatedAt())
//                .imageUrls(ad.getImages().stream()
//                        .map(Image::getImageUrl)
//                        .collect(Collectors.toList()))
//                .build());
//    }


    //
//    public Page<AdDTOSpecific> getAllNewAds(Pageable pageable) {
//        Page<Ad> adPage = adRepository.findByIsNewTrueAndStatus(pageable, AdStatus.APPROVED);
//
//        return adPage.map(ad -> AdDTOSpecific.builder()
//                .id(ad.getId())
//                .categoryId(ad.getCategory().getId())
//                .modelId(ad.getModel().getId())
//                .price(ad.getPrice())
//                .header(ad.getHeader())
//                .createdAt(ad.getCreatedAt())
//                .imageUrls(ad.getImages().stream()
//                        .map(Image::getImageUrl)
//                        .collect(Collectors.toList()))
//                .build());
//    }


    //    public Page<AdDTOSpecific> getSuggestions(String searchQuery, Pageable pageable) {
//        Page<Ad> suggestions = adRepository.findSuggestions(searchQuery, pageable);
//        System.out.println("SearchQuery " + searchQuery);
//
//
//        return suggestions.map(ad -> AdDTOSpecific.builder()
//                .id(ad.getId())
//                .categoryId(ad.getCategory().getId())
//                .modelId(ad.getModel().getId())
//                .price(ad.getPrice())
//                .header(ad.getHeader())
//                .createdAt(ad.getCreatedAt())
//                .imageUrls(ad.getImages().stream()
//                        .map(Image::getImageUrl)
//                        .collect(Collectors.toList()))
//                .build());
//    }

    //
//    public Page<AdDTOSpecific> search(String searchQuery, Pageable pageable){
//        Specification<Ad> specification = AdSpecification.searchByBrandOrModel(searchQuery);
//        Page<Ad> ads=adRepository.findAll(specification,pageable);
//
//        return ads.map(ad -> AdDTOSpecific.builder()
//                .id(ad.getId())
//                .categoryId(ad.getCategory().getId())
//                .modelId(ad.getModel().getId())
//                .price(ad.getPrice())
//                .header(ad.getHeader())
//                .createdAt(ad.getCreatedAt())
//                .imageUrls(ad.getImages().stream()
//                        .map(Image::getImageUrl)
//                        .collect(Collectors.toList()))
//                .build());
//    }

}

