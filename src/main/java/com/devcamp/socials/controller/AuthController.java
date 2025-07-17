package com.devcamp.socials.controller;

import com.devcamp.socials.dto.Response;
import com.devcamp.socials.dto.SignInRequest;
import com.devcamp.socials.dto.SignInResponse;
import com.devcamp.socials.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Endpoints for signing in users")
public class AuthController {
  private final AuthService authService;

  @PostMapping("/sign-in")
  @Operation(
      summary = "User Sign-In",
      description = "Authenticate user and return JWT token",
      responses = {
        @ApiResponse(responseCode = "200", description = "Successfully authenticated"),
        @ApiResponse(responseCode = "401", description = "Invalid username or password.")
      })
  public ResponseEntity<Response<SignInResponse>> signIn(
      @RequestBody @Valid SignInRequest request) {
    SignInResponse response = authService.signIn(request);
    return ResponseEntity.ok(Response.success(response));
  }
}
