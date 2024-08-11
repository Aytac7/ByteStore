package com.example.startapp.controller.common;

import com.example.startapp.dto.request.common.AdRequest;
import com.example.startapp.dto.response.common.AdResponse;
import com.example.startapp.service.common.AdService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdController {

    private final AdService adService;

    @PostMapping
    public ResponseEntity<AdResponse> createAd(
            @Valid @RequestPart("adRequest") AdRequest adRequest,
            @RequestPart("images") List<MultipartFile> files) {
        AdResponse adResponse = adService.createAd(adRequest, files);
        return new ResponseEntity<>(adResponse, HttpStatus.CREATED);
    }
}
