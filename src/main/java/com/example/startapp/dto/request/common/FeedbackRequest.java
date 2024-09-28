package com.example.startapp.dto.request.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackRequest {

    String fullName;

    String phoneNumber;

    String description;
}
