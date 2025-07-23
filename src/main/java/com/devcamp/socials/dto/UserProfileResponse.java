package com.devcamp.socials.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileResponse {
  private Long id;
  private String firstName;
  private String lastName;
  private String username;
  private LocalDate birthDate;
}
