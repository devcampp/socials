package com.devcamp.socials.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record SignUpRequest(
    @NotBlank(message = "MISSING_REQUIRED_FIELDS") String username,
    @NotBlank(message = "MISSING_REQUIRED_FIELDS") String password,
    @NotBlank(message = "MISSING_REQUIRED_FIELDS") String firstName,
    @NotBlank(message = "MISSING_REQUIRED_FIELDS") String lastName,
    @NotNull(message = "MISSING_REQUIRED_FIELDS") LocalDate birthDate) {}
