package com.devcamp.socials.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoleEntity {
  private Long id;
  private String name;
}
