package com.example.startapp.controller.common;

import com.example.startapp.dto.response.common.BrandDTO;
import com.example.startapp.dto.response.common.CategoryDTO;
import com.example.startapp.dto.response.common.ModelDTO;
import com.example.startapp.service.common.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("categories")
@RequiredArgsConstructor

public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/all-categories")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/brands/{categoryId}")
    public ResponseEntity<List<BrandDTO>> getBrandsByCategory(@PathVariable Long categoryId) {
        List<BrandDTO> brands = categoryService.getBrandsByCategory(categoryId);
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/models/{brandId}")
    public ResponseEntity<List<ModelDTO>> getModelsByBrand(@PathVariable Long brandId) {
        List<ModelDTO> models = categoryService.getModelsByBrand(brandId);
        return ResponseEntity.ok(models);
    }

}
