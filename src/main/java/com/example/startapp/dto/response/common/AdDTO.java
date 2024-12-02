package com.example.startapp.dto.response.common;

import com.example.startapp.entity.common.Favorite;
import com.example.startapp.enums.PhonePrefix;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdDTO {
     Long id;
     double price;
     Long userId;
     Long modelId;
     Long brandId;
     String header;
     Boolean isNew;
     String status;
     Long categoryId;
     String phoneNumber;
     String additionalInfo;
     List<String> imageUrls;
     LocalDateTime createdAt;
     PhonePrefix phonePrefix;
     String city;
     Favorite favorite;
     boolean isFavorite;
     String modelName;
     String brandName;
     String categoryName;
}
