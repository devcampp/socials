package com.devcamp.socials.mapper;

import com.devcamp.socials.dto.SignUpRequest;
import com.devcamp.socials.dto.UserProfileResponse;
import com.devcamp.socials.entity.RoleEntity;
import com.devcamp.socials.entity.UserEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  UserProfileResponse toDto(UserEntity user);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "enabled", constant = "true")
  @Mapping(target = "password", source = "encodedPassword")
  UserEntity toEntity(SignUpRequest request, String encodedPassword, List<RoleEntity> roles);
}
