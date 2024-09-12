package com.example.startapp.service.common;

import com.example.startapp.dto.response.common.AdDTOSpecific;
import com.example.startapp.entity.common.Ad;
import com.example.startapp.entity.common.Favorite;
import com.example.startapp.entity.common.Image;
import com.example.startapp.entity.auth.User;
import com.example.startapp.exception.AdNotFoundException;
import com.example.startapp.repository.auth.UserRepository;
import com.example.startapp.repository.common.AdRepository;
import com.example.startapp.repository.common.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final AdRepository adRepository;
    private final UserRepository userRepository;

    @Transactional
    public String toggleFavoriteAd(Long userId, Long adId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı, ID: " + userId));

        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new AdNotFoundException("Elan tapılmadı, ID: " + adId));

        boolean isFavorite = favoriteRepository.existsByUserUserIdAndAdId(user.getUserId(), ad.getId());

        if (isFavorite) {
            favoriteRepository.deleteByUserUserIdAndAdId(user.getUserId(), ad.getId());
            return "Elan favoritlərdən silindi";

        } else {
            Favorite favorite = Favorite.builder()
                    .user(user)
                    .ad(ad)
                    .build();
            favoriteRepository.save(favorite);
            return "Elan favoritlərə əlavə edildi";
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
