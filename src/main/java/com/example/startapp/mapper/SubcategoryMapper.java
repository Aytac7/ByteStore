package com.example.startapp.mapper;

import com.example.startapp.dto.response.common.SubcategoryDTO;
import com.example.startapp.entity.common.Subcategory;

public class SubcategoryMapper {

    public static SubcategoryDTO subcategoryDTO(Subcategory subcategory){
        return SubcategoryDTO.builder()
                .name(subcategory.getName())
                .id(subcategory.getId())
                .build();
    }
}
