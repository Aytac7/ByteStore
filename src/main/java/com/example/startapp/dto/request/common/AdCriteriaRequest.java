package com.example.startapp.dto.request.common;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdCriteriaRequest {

    Long categoryId;
    List<Long> brandIds;
    List<Long> modelIds;
    double minPrice;
    double maxPrice;


    String searchText;


    Boolean newSelected = false;
    Boolean secondhandSelected = false;

    Boolean sortByPriceAsc = false;
    Boolean sortByPriceDesc = false;

    Boolean sortByDate = false;

}
