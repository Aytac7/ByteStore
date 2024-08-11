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

@Service
@RequiredArgsConstructor
public class AdService {

    private final AdRepository adRepository;
    private final AdMapper adMapper;

    public AdResponse createAd(AdRequest adRequest) {
        Ad ad = adMapper.mapToEntity(adRequest);
        Ad savedAd = adRepository.save(ad);
        return adMapper.mapToResponse(savedAd);

    }
}