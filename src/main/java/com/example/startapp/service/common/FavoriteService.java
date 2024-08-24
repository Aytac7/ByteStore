package com.example.startapp.service.common;

import com.example.startapp.dto.response.common.AdDTO;
import com.example.startapp.dto.response.common.AdDTOSpecific;
import com.example.startapp.entity.Ad;
import com.example.startapp.entity.Favorite;
import com.example.startapp.entity.Image;
import com.example.startapp.entity.User;
import com.example.startapp.repository.UserRepository;
import com.example.startapp.repository.common.AdRepository;
import com.example.startapp.repository.common.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class FavoriteService {

    private static final Logger logger = LoggerFactory.getLogger(FavoriteService.class);
    private final FavoriteRepository favoriteRepository;
    private final AdRepository adRepository;
    private final UserRepository userRepository;

    public void toggleFavorite(Long userId, Long adId) {
        logger.info("Toggling favorite for userId: {} and adId: {}");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        if (favoriteRepository.existsByUserUserIdAndAdId(user.getUserId(), ad.getId())) {
            logger.info("Favorite exists, deleting...");
            favoriteRepository.deleteByUserUserIdAndAdId(user.getUserId(), ad.getId());
        } else {
            logger.info("Favorite does not exist, creating...");
            Favorite favorite = Favorite.builder()
                    .user(user)
                    .ad(ad)
                    .build();
            favoriteRepository.save(favorite);
        }
    }

    public Page<AdDTOSpecific> getFavoritesForUser(Long userId, Pageable pageable) {
        Page<Favorite> favoritesPage = favoriteRepository.findByUserUserId(userId, pageable);

        return favoritesPage.map(favorite -> {
            Ad ad = favorite.getAd();
            return AdDTOSpecific.builder()
                    .id(ad.getId())
                    .categoryId(ad.getCategory().getId())
                    .modelId(ad.getModel().getId())
                    .price(ad.getPrice())
                    .header(ad.getHeader())
                    .createdAt(ad.getCreatedAt())
                    .imageUrls(ad.getImages().stream()
                            .map(Image::getImageUrl)
                            .collect(Collectors.toList()))
                    .build();
        });
    }



}
