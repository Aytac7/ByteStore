package com.example.startapp.repository.common;

import com.example.startapp.entity.common.Feedbacks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbacksRepository extends JpaRepository<Feedbacks, Long> {
}
