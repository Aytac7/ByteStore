package com.example.startapp.controller.common;

import com.example.startapp.dto.request.common.AdCriteriaRequest;
import com.example.startapp.dto.request.common.AdRequest;

import com.example.startapp.dto.response.common.*;
import com.example.startapp.entity.auth.User;
import com.example.startapp.enums.AdStatus;
import com.example.startapp.service.common.AdService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
    public ResponseEntity<Page<AdDTOSpecific>> getAdsWithFilter(
           @RequestParam AdCriteriaRequest adCriteriaRequest,
            Pageable pageable) {

        Page<AdDTOSpecific> ads = adService.getAdsWithFilter(adCriteriaRequest, pageable);

        if (ads.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(ads);
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createAd(
            @RequestParam("adRequest") String adRequestJson,
            @RequestParam("files") List<MultipartFile> files) {

        if (adRequestJson == null || adRequestJson.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Elan haqqında məlumat yoxdu");
        }

        if (files == null || files.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Şəkil əlavə olunmayıb");
        }

        try {
            AdRequest adRequest = objectMapper.readValue(adRequestJson, AdRequest.class);
            adService.createAd(adRequest, files);
            return ResponseEntity.status(HttpStatus.CREATED).body("Ad added successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid adRequest data");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating advertisement");
        }
    }

    @PutMapping("/update/{adId}")
    public ResponseEntity<String> update(@PathVariable Long adId, @RequestParam("adRequest") String adRequestJson, @RequestParam("files") List<MultipartFile> files) {
        AdRequest adRequest;
        try {
            adRequest = objectMapper.readValue(adRequestJson, AdRequest.class);
        } catch (IOException e) {
            log.error("Error parsing adRequest JSON", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request data");
        }

        try {
            adService.updateAd(adId, adRequest, files);
            return ResponseEntity.status(HttpStatus.OK).body("Ad changed successfully");
        } catch (Exception e) {
            log.error("Error creating advertisement", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating advertisement");
        }
    }

    @GetMapping("/myAds/{status}")
    public ResponseEntity<List<AdDTO>> getMyAds(@AuthenticationPrincipal User user, @PathVariable AdStatus status) {
        List<AdDTO> myAds = adService.getUserAdsByStatus(user.getUserId(), status);
        return ResponseEntity.ok(myAds);
    }


    @GetMapping("/{adId}")
    public ResponseEntity<AdDTO> getAd(@PathVariable Long adId) {
        AdDTO ad = adService.getAdById(adId);
        return ResponseEntity.ok(ad);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AdDTO>> getAllAds() {
        List<AdDTO> ads = adService.getAllAds();
        return ResponseEntity.ok(ads);
    }

    @GetMapping("/new")
    public Page<AdDTOSpecific> getAllNewAds(Pageable pageable) {
        return adService.getAllNewAds(pageable);
    }

    @GetMapping("/secondhand")
    public Page<AdDTOSpecific> getAllSecondHandAds(Pageable pageable) {
        return adService.getAllSecondHandAds(pageable);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AdDTO>> getAdByUserId(@PathVariable Long userId) {
        List<AdDTO> ads = adService.getAdsByUserId(userId);
        return ResponseEntity.ok(ads);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAd(@PathVariable Long id) {
        adService.deleteAdById(id);
        return ResponseEntity.ok("Ad deleted successfully");
    }




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
}