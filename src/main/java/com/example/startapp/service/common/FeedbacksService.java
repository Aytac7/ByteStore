package com.example.startapp.service.common;

import com.amazonaws.services.guardduty.model.Feedback;
import com.example.startapp.dto.request.common.FeedbackRequest;
import com.example.startapp.entity.common.Feedbacks;
import com.example.startapp.repository.common.FeedbacksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class FeedbacksService {
    private final FeedbacksRepository feedbackRepository;

    public void addFeedback(FeedbackRequest feedbackRequest) {
        Feedbacks feedback = Feedbacks.builder()
                .phoneNumber(feedbackRequest.getPhoneNumber())
                .fullName(feedbackRequest.getFullName())
                .description(feedbackRequest.getDescription())
                .build();

        feedbackRepository.save(feedback);
    }

}
