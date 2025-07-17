package com.devcamp.socials.mapper;

import com.devcamp.socials.dto.UserResponse;
import com.devcamp.socials.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  UserResponse toDto(UserEntity user);
}
