package com.devcamp.socials.dto;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
    @NotBlank(message = "MISSING_REQUIRED_FIELDS") String username,
    @NotBlank(message = "MISSING_REQUIRED_FIELDS") String password) {}
