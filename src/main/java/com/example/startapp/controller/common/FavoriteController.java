package com.example.startapp.controller.common;

import com.example.startapp.dto.response.common.AdDTO;
import com.example.startapp.dto.response.common.AdDTOSpecific;
import com.example.startapp.entity.User;
import com.example.startapp.service.common.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/{userId}/toggle/{adId}")
    public ResponseEntity<String> toggleFavorite(
            @PathVariable Long userId,
            @PathVariable Long adId) {
        try {
            String result = favoriteService.toggleFavoriteAd(userId, adId);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public Page<AdDTOSpecific> getFavoritesForUser(
            @PathVariable Long userId,
            Pageable pageable) {
        return favoriteService.getFavoritesForUser(userId, pageable);
    }
}
