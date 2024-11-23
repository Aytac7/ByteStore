package com.example.startapp.dto.response.common;

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
public class AdDTOSpecific {
     Long id;
     double price;
     Long modelId;
     String modelName;
     String header;
     Long categoryId;
     String categoryName;
     boolean isFavorite;
     LocalDateTime createdAt;
     List<String> imageUrls;


}
