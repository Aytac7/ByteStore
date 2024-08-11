package com.example.startapp.service.common;

import com.example.startapp.dto.request.common.AdRequest;
import com.example.startapp.dto.response.common.AdResponse;
import com.example.startapp.entity.*;
import com.example.startapp.mapper.AdMapper;
import com.example.startapp.repository.UserRepository;
import com.example.startapp.repository.common.AdRepository;
import com.example.startapp.repository.common.BrandRepository;
import com.example.startapp.repository.common.CategoryRepository;
import com.example.startapp.repository.common.ModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdService {
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ModelRepository modelRepository;
    private final AdMapper adMapper;


    public AdResponse createAd(AdRequest adRequest, List<MultipartFile> files) {
        Ad ad = adMapper.mapToEntity(adRequest);

        List<Image> images = adMapper.mapMultipartFilesToImages(files, ad);
        ad.setImages(images);

        Ad savedAd = adRepository.save(ad);

        return adMapper.mapToResponse(savedAd);
    }
}