package com.example.startapp.mapper;

import com.example.startapp.dto.request.common.AdRequest;
import com.example.startapp.dto.response.common.AdResponse;
import com.example.startapp.entity.Ad;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AdMapper {

    Ad  mapToEntity(AdRequest adRequest);

    AdResponse mapToResponse(Ad ad);
}
