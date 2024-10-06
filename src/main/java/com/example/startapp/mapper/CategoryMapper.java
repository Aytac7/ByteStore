package com.example.startapp.mapper;

import com.example.startapp.dto.response.common.CategoryDTO;
import com.example.startapp.entity.common.Category;

public class CategoryMapper {
    public static CategoryDTO toDTO(Category category) {
        return CategoryDTO.builder()
                .name(category.getName())
                .id(category.getId())
                .build();
    }
}
