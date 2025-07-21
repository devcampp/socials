package com.devcamp.socials.controller;

import com.devcamp.socials.dto.Response;
import com.devcamp.socials.dto.UserResponse;
import com.devcamp.socials.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "Endpoints for retrieving and managing users")
public class UserController {
  private final UserService service;

  @GetMapping("/{id}")
  @Operation(
      summary = "Get user by ID",
      description =
          "Retrieves the user profile for the given user ID, including basic user information.",
      responses = {
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "User with the given ID was not found")
      })
  public ResponseEntity<Response<UserResponse>> getUser(@PathVariable Long id) {
    UserResponse response = service.getUserRepository(id);
    return ResponseEntity.ok(Response.success(response));
  }

  @GetMapping
  @Operation(
      summary = "Getting users profile, filters: first name, last name",
      description =
          "Retrieves the users for the given user first name and last name, including basic user information.",
      responses = {
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
      })
  public ResponseEntity<Response<List<UserResponse>>> getUsers(
      @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) {
    List<UserResponse> response = service.getUsers(firstName, lastName);
    return ResponseEntity.ok(Response.success(response));
  }
}
