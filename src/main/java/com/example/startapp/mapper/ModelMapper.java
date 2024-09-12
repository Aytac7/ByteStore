package com.example.startapp.mapper;

import com.example.startapp.dto.response.common.ModelDTO;
import com.example.startapp.entity.common.Model;

public class ModelMapper {

    public static ModelDTO modelDTO(Model model){
        return ModelDTO.builder()
                .name(model.getName())
                .build();
    }
}
