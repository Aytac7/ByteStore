package com.example.startapp.controller.common;

import com.example.startapp.dto.request.common.AdRequest;
import com.example.startapp.dto.response.common.AdResponse;
import com.example.startapp.entity.Ad;
import com.example.startapp.service.common.AdService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdController {

    private final AdService adService;

    @PostMapping("/createAd")
    public ResponseEntity<Ad> createAd(@Valid @RequestBody AdRequest adRequest) {
        Ad createdAd = adService.createNewAd(adRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAd);
    }
}
