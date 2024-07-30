package com.example.startapp.mapper;

import com.example.startapp.dto.request.RegisterRequest;
import com.example.startapp.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    @Mapping(source = "phonePrefix", target = "phonePrefix")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "email", target = "email")
    User mapToUser(RegisterRequest request);
}
