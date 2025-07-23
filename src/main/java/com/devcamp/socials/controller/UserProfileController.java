package com.devcamp.socials.controller;

import com.devcamp.socials.dto.Response;
import com.devcamp.socials.dto.UserProfileResponse;
import com.devcamp.socials.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profiles")
@Tag(name = "User Profiles", description = "Endpoints for retrieving and managing user profiles")
public class UserProfileController {
  private final UserProfileService service;

  @GetMapping("/{id}")
  @Operation(
      summary = "Get user profile by ID",
      description =
          "Retrieves the complete user profile for the given user ID, including "
              + "profile information, social metrics (followers, following, posts count), "
              + "and verification status.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "User profile retrieved successfully"),
    @ApiResponse(responseCode = "404", description = "User profile with the given ID was not found")
  })
  public ResponseEntity<Response<UserProfileResponse>> getUserProfile(
      @Parameter(description = "User ID", example = "123", required = true) @PathVariable Long id) {

    UserProfileResponse profile = service.getProfileById(id);
    return ResponseEntity.ok(Response.success(profile));
  }

  @GetMapping
  @Operation(
      summary = "Search user profiles with filters",
      description =
          "Search and retrieve user profiles using various filters such as first name, last name")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "User profiles retrieved successfully"),
    @ApiResponse(
        responseCode = "400",
        description = "At least one search parameter must be provided")
  })
  public ResponseEntity<Response<List<UserProfileResponse>>> searchProfiles(
      @Parameter(
              description = "First name to search for (partial match supported)",
              example = "John")
          @RequestParam(value = "firstName", required = false)
          String firstName,
      @Parameter(description = "Last name to search for (partial match supported)", example = "Doe")
          @RequestParam(value = "lastName", required = false)
          String lastName) {
    List<UserProfileResponse> profiles = service.searchProfiles(firstName, lastName);
    return ResponseEntity.ok(Response.success(profiles));
  }
}
