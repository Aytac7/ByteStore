package com.example.startapp.dto.response.common;

import com.example.startapp.enums.PhonePrefix;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdDTO {
    private Long id;
    private Long price;
    private String header;
    private String additionalInfo;
    private Boolean isNew;
    private Long userId;
    private Long categoryId;
    private Long brandId;
    private Long modelId;
    private List<String> imageUrls;
    private PhonePrefix phonePrefix;
    private String phoneNumber;
    private String status;
}
