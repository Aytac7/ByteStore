package com.example.startapp.controller.common;

import com.example.startapp.dto.request.common.FeedbackRequest;
import com.example.startapp.service.common.FeedbacksService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor

public class FeedbacksController {
    private final FeedbacksService feedbackService;

    @PostMapping("/add")
    public void addFeedback(@RequestBody FeedbackRequest feedback) {
        feedbackService.addFeedback(feedback);

    }


}
