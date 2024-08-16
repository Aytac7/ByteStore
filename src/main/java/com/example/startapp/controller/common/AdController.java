package com.example.startapp.controller.common;

import com.example.startapp.dto.request.common.AdRequest;

import com.example.startapp.service.common.AdService;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    @PostMapping("/create")
    public ResponseEntity<String> createAd(
            @RequestParam("adRequest") String adRequestJson,
            @RequestParam("files") List<MultipartFile> files) {

        AdRequest adRequest;
        try {
            adRequest = objectMapper.readValue(adRequestJson, AdRequest.class);
        } catch (IOException e) {
            log.error("Error parsing adRequest JSON", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request data");
        }

        try {
            adService.createAd(adRequest, files);
            return ResponseEntity.status(HttpStatus.CREATED).body("Ad added successfully");
        } catch (Exception e) {
            log.error("Error creating advertisement", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating advertisement");
        }
    }
}