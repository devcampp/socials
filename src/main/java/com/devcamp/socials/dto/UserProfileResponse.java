package com.devcamp.socials.dto;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record UserProfileResponse(Long id, String firstName, String lastName, String username,
                                  LocalDate birthDate) {

}
