package com.example.startapp.repository.common;

import com.example.startapp.entity.common.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
//    boolean existsByUserAndAd(User user, Ad ad);
//    void deleteByUserAndAd(User user, Ad ad);

    boolean existsByUserUserIdAndAdId(Long userId, Long adId);
    void deleteByUserUserIdAndAdId(Long userId, Long adId);
    Page<Favorite> findByUserUserId(Long userId, Pageable pageable);
    List<Favorite> findByUserUserId (Long userId);

}

