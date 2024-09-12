package com.example.startapp.dto.request.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AdCriteriaRequest {

    double priceTo;
    double priceFrom;
    List<Long> brandIds;
    List<Long> modelIds;
    boolean sortByNewest;
    Boolean isNew;

}
