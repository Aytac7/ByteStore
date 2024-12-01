package com.example.startapp.repository.common;

import com.example.startapp.dto.response.common.CategoryDTO;
import com.example.startapp.entity.common.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT DISTINCT c FROM Category c " +
            "LEFT JOIN FETCH c.brands b " +
            "LEFT JOIN FETCH b.models m " +
            "WHERE m.category = c")
    List<Category> findAllWithBrandsAndModels();
}
