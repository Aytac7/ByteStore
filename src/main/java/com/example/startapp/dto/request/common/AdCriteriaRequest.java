package com.example.startapp.dto.request.common;

import com.example.startapp.enums.AdStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AdCriteriaRequest {

    Long userId;
    AdStatus status;
    String header;
}
