package com.example.startapp.dto.response.common;

import com.example.startapp.enums.AdStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdResponse {

     Long id;
     Long price;
     String header;
     String additionalInfo;
     Boolean isNew;
     String phoneNumber;
     UserResponseForAd userName;
     CategoryResponseForAd categoryName;
     BrandResponseForAd brandName;
     ModelResponseForAd modelName;
     AdStatus status;
     List<String> imageUrls;
     LocalDateTime createdAt;
     LocalDateTime updatedAt;
}
