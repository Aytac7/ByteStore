package com.example.startapp.controller.common;

import com.example.startapp.dto.request.common.AdRequest;
import com.example.startapp.dto.response.common.AdResponse;
import com.example.startapp.entity.Ad;
import com.example.startapp.entity.Image;
import com.example.startapp.mapper.AdMapper;
import com.example.startapp.repository.common.AdRepository;
import com.example.startapp.service.common.AdService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/ads")
    public ResponseEntity<AdResponse> createAd(
            @RequestParam("adRequest") String adRequestJson,
            @RequestParam("files") List<MultipartFile> files) {

        AdRequest adRequest;
        try {
            // Convert JSON string to AdRequest object
            adRequest = objectMapper.readValue(adRequestJson, AdRequest.class);

        } catch (IOException e) {
            log.error("Error parsing adRequest JSON", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            AdResponse adResponse = adService.createAd(adRequest, files);
            return ResponseEntity.status(HttpStatus.CREATED).body(adResponse);
        } catch (Exception e) {
            log.error("Error creating advertisement", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
