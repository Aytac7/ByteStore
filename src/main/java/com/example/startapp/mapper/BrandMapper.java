package com.example.startapp.mapper;

import com.example.startapp.dto.response.common.BrandDTO;
import com.example.startapp.entity.common.Brand;

public class BrandMapper {

    public static BrandDTO brandDTO(Brand brand){
       return BrandDTO.builder()
               .name(brand.getName())
               .id(brand.getId())
                .build();
    }
}
