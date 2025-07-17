package com.devcamp.socials.dto;

import java.time.LocalDate;

public record UserResponse(
    Long id,
    String firstName,
    String lastName,
    String username,
    LocalDate birthDate,
    boolean enabled) {}
