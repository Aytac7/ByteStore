package com.example.startapp.repository.common;

import com.example.startapp.entity.common.Ad;
import com.example.startapp.enums.AdStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long>,
        PagingAndSortingRepository<Ad, Long>, JpaSpecificationExecutor<Ad> {

    @Query("SELECT a FROM Ad a WHERE (LOWER(a.brand.name) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR LOWER(a.model.name) LIKE LOWER(CONCAT('%', :searchQuery, '%'))) AND a.status = 'APPROVED'")
    Page<Ad> findSuggestions(@Param("searchQuery") String searchQuery, Pageable pageable);

    List<Ad> findByModelIdAndStatus(Long modelId,AdStatus adStatus);
    List<Ad> findAllByStatus(AdStatus status);

    List<Ad> findAllByCreatedAtBefore(LocalDateTime createdAt);

    List<Ad> findByUser_UserId(Long userId);

    List<Ad> findByUser_UserIdAndStatus(Long userId, AdStatus status);

    Page<Ad> findByIsNewTrueAndStatus(Pageable pageable, AdStatus status);

    Page<Ad> findByIsNewFalseAndStatus(Pageable pageable, AdStatus status);


}
