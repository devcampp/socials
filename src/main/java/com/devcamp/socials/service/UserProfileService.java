package com.devcamp.socials.service;

import com.devcamp.socials.dto.UserProfileResponse;
import com.devcamp.socials.enums.MessageKey;
import com.devcamp.socials.exception.ApiException;
import com.devcamp.socials.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileService {
  private final UserRepository userRepository;

  public UserProfileResponse getProfileById(Long userId) {
    log.debug("Retrieving user profile for ID: {}", userId);

    UserProfileResponse profile = userRepository.getProfileById(userId);

    if (profile == null) {
      throw new ApiException(MessageKey.USER_NOT_FOUND);
    }

    log.debug("Successfully retrieved profile for user ID: {}", userId);
    return profile;
  }
}
