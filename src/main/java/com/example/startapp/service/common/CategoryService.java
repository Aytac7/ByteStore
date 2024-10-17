package com.example.startapp.service.common;

import com.example.startapp.dto.response.common.BrandDTO;
import com.example.startapp.dto.response.common.CategoryDTO;
import com.example.startapp.dto.response.common.ModelDTO;
import com.example.startapp.entity.common.Brand;
import com.example.startapp.entity.common.Category;
import com.example.startapp.exception.BrandNotFoundException;
import com.example.startapp.exception.CategoryNotFoundException;
import com.example.startapp.mapper.BrandMapper;
import com.example.startapp.mapper.CategoryMapper;
import com.example.startapp.mapper.ModelMapper;
import com.example.startapp.repository.common.BrandRepository;
import com.example.startapp.repository.common.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;


    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<BrandDTO> getBrandsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException("Kateqoriya tapilmadi"));
        return category.getBrands().stream()
                .map(BrandMapper::brandDTO)
                .collect(Collectors.toList());

    }

    public List<ModelDTO> getModelsByBrand(Long brandId) {
        Brand brand = brandRepository.findById(brandId).orElseThrow(() -> new BrandNotFoundException("Marka tapilmadi"));
        return brand.getModels().stream()
                .map(ModelMapper::modelDTO)
                .collect(Collectors.toList());
    }



}
