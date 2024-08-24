package com.example.startapp.controller.common;

import com.example.startapp.dto.response.common.AdDTO;
import com.example.startapp.dto.response.common.AdDTOSpecific;
import com.example.startapp.entity.User;
import com.example.startapp.service.common.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/toggle/{userId}/{adId}")
    public ResponseEntity<String> toggleFavorite(
            @PathVariable Long userId,
            @PathVariable Long adId) {
        favoriteService.toggleFavorite(userId, adId);
        return ResponseEntity.ok("ad added to favorite");
    }

    @GetMapping("/{userId}")
    public Page<AdDTOSpecific> getFavoritesForUser(
            @PathVariable Long userId,
            Pageable pageable) {

        return favoriteService.getFavoritesForUser(userId, pageable);
    }
}
