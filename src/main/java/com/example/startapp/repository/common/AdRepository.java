package com.example.startapp.repository.common;

import com.example.startapp.entity.Ad;
import com.example.startapp.enums.AdStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long>,
        PagingAndSortingRepository<Ad, Long>, JpaSpecificationExecutor<Ad> {

    List<Ad> findAllByStatus(AdStatus status);
    Ad findByIdAndStatus(Long id,AdStatus status);
    List<Ad> findAllByCreatedAtBeforeAndStatus(LocalDateTime createdAt,AdStatus status);

}
