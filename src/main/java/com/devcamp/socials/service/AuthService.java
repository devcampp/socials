package com.devcamp.socials.service;

import com.devcamp.socials.dto.SignInRequest;
import com.devcamp.socials.dto.SignInResponse;
import com.devcamp.socials.enums.MessageKey;
import com.devcamp.socials.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  public SignInResponse signIn(SignInRequest request) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.username(), request.password()));
      String token = jwtService.generateToken(request.username());
      return new SignInResponse(token);
    } catch (BadCredentialsException e) {
      log.debug("Bad credentials, trying to sign in, username: {}", request.username());

      throw new ApiException(MessageKey.BAD_CREDENTIALS);
    } catch (DisabledException e) {
      log.debug("User is disabled, trying to sign in, username: {}", request.username());

      throw new ApiException(MessageKey.USER_DISABLED);
    }
  }
}
