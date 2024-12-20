package com.example.startapp.controller.common;

import com.example.startapp.dto.request.common.AdCriteriaRequest;
import com.example.startapp.dto.request.common.AdRequest;

import com.example.startapp.dto.response.common.*;
import com.example.startapp.enums.AdStatus;
import com.example.startapp.service.common.AdService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdController {

    private final AdService adService;
    private final ObjectMapper objectMapper;


    @GetMapping("/model/{modelId}")
    public ResponseEntity<List<AdDTO>> getAdsByModel(@PathVariable Long modelId) {
        List<AdDTO> ads = adService.getAdsByModel(modelId);
        return ResponseEntity.ok(ads);
    }


    @GetMapping("/filter")
    public ResponseEntity<Map<String, Object>> getAdsWithFilter(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            AdCriteriaRequest adCriteriaRequest,
            Pageable pageable) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            Map<String, Object> ads = adService.getAdsWithFilter(token, adCriteriaRequest, pageable);
            if (ads.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(ads);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createAd(
            @RequestParam("adRequest") String adRequestJson,
            @RequestParam("files") List<MultipartFile> files,
            @RequestHeader(value = "Authorization") String authorizationHeader) {


        if (adRequestJson == null || adRequestJson.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Elan haqqında məlumat yoxdu");
        }

        if (files == null || files.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Şəkil əlavə olunmayıb");
        }

        try {
            String token = authorizationHeader.replace("Bearer ", "");
            AdRequest adRequest = objectMapper.readValue(adRequestJson, AdRequest.class);
            adService.createAd(adRequest, files, token);
            return ResponseEntity.status(HttpStatus.CREATED).body("Ad created successfully");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Invalid adRequest data");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating advertisement");
        }
    }

    @DeleteMapping("/delete/{adId}")
    public ResponseEntity<String> deleteAd(
            @PathVariable Long adId,
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            adService.deleteAdById(adId, token);
            return ResponseEntity.ok("Ad deleted successfully");

        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }


    @PutMapping("/update/{adId}")
    public ResponseEntity<?> update(@PathVariable Long adId,
                                    @RequestParam("adRequest") String adRequestJson,
                                    @RequestParam("files") List<MultipartFile> files,
                                    @RequestHeader("Authorization") String authorizationHeader) {
        try {
            AdRequest adRequest = objectMapper.readValue(adRequestJson, AdRequest.class);
            String token = authorizationHeader.replace("Bearer ", "");
            adService.updateAd(adId, adRequest, files, token);
            return ResponseEntity.ok("Ad updated successfully");
        } catch (Exception e) {
            log.error("Error updating advertisement", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/myAds")
    public ResponseEntity<Map<String, Object>> getUserAdsByStatus(
            @RequestHeader(value = "Authorization") String authorizationHeader,
            @RequestParam("status") AdStatus status,
            Pageable pageable) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Authorization token is required"));
        }
        String token = authorizationHeader.replace("Bearer ", "");
        if (!isValidJwt(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid JWT token"));
        }
        Map<String, Object> response = adService.getUserAdsByStatus(token, status, pageable);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{adId}")
    public ResponseEntity<AdDTO> getAd(@PathVariable Long adId) {
        AdDTO ad = adService.getAdById(adId);
        return ResponseEntity.ok(ad);
    }


    //    @GetMapping("/all")
//    public ResponseEntity<List<AdDTO>> getAllAds() {
//        List<AdDTO> ads = adService.getAllAds();
//        return ResponseEntity.ok(ads);
//    }

    @GetMapping("/user")
    public Map<String, Object> getAdByUserId(
            @RequestHeader("Authorization") String authorizationHeader,
            Pageable pageable) {

        String token = authorizationHeader.replace("Bearer ", "");
        Map<String, Object> ads = adService.getAdsByUserId(token, pageable);
        return ResponseEntity.ok(ads).getBody();
    }

    @GetMapping("/new")
    public ResponseEntity<?> getAllNewAds(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            Pageable pageable) {
        String token = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.replace("Bearer ", "").trim();

            if (!isValidJwt(token)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JWT token format");
            }
        }

        Map<String, Object> response = adService.getAllNewAds(token, pageable);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/secondhand")
    public ResponseEntity<?> getAllSecondHandAds(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            Pageable pageable) {
        String token = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.replace("Bearer ", "").trim();

            if (!isValidJwt(token)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JWT token format");
            }
        }

        Map<String, Object> response = adService.getAllSecondHandAds(token, pageable);
        return ResponseEntity.ok(response);
    }

    private boolean isValidJwt(String token) {
        return token != null && !token.isEmpty() && token.split("\\.").length == 3;
    }
}


//
//
//    @GetMapping("/search/suggestions")
//    public ResponseEntity<?> findSuggestions(@RequestParam String searchQuery, Pageable pageable) {
//        Page<AdDTOSpecific> suggestions = adService.getSuggestions(searchQuery, pageable);
//
//        if (suggestions.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Heç bir uyğun nəticə tapılmadı.");
//        }
//
//        return ResponseEntity.ok(suggestions);
//    }
