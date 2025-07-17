package com.devcamp.socials.controller;

import com.devcamp.socials.dto.Response;
import com.devcamp.socials.dto.SignInRequest;
import com.devcamp.socials.dto.SignInResponse;
import com.devcamp.socials.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
  private final AuthService authService;

  @PostMapping("/sign-in")
  public ResponseEntity<Response<SignInResponse>> signIn(SignInRequest request) {
    SignInResponse response = authService.signIn(request);
    return ResponseEntity.ok(Response.success(response));
  }
}
