package com.example.startapp.repository.common;

import com.example.startapp.entity.Favorite;
import jakarta.xml.ws.RespectBinding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

}
