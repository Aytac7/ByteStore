package com.example.startapp.controller.common;

import com.example.startapp.dto.response.common.AdDTOSpecific;
import com.example.startapp.exception.AdNotFoundException;
import com.example.startapp.service.common.FavoriteService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/toggle/{adId}")
    public ResponseEntity<String> toggleFavorite(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long adId) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            String result = favoriteService.toggleFavoriteAd(token, adId);
            return ResponseEntity.ok(result);

        } catch (AdNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/myFavs")
    public ResponseEntity<Page<AdDTOSpecific>> getFavoritesForUser(
            @RequestHeader("Authorization") String authorizationHeader,
            Pageable pageable) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            Page<AdDTOSpecific> favorites = favoriteService.getFavoritesForUser(token, pageable);

            if (favorites.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(favorites);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
