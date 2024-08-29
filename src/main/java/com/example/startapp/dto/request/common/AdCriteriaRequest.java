package com.example.startapp.dto.request.common;

import com.example.startapp.entity.Brand;
import com.example.startapp.entity.Model;
import com.example.startapp.enums.AdStatus;
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
