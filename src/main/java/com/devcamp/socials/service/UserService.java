package com.devcamp.socials.service;

import com.devcamp.socials.dto.UserResponse;
import com.devcamp.socials.entity.UserEntity;
import com.devcamp.socials.enums.MessageKey;
import com.devcamp.socials.exception.ApiException;
import com.devcamp.socials.mapper.UserMapper;
import com.devcamp.socials.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
  private final UserRepository repository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return repository
        .findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(username));
  }

  public UserResponse getUser(Long id) {
    UserEntity user =
        repository
            .findById(id)
            .orElseThrow(() -> new ApiException(MessageKey.USER_NOT_FOUND, "id: " + id));

    return UserMapper.INSTANCE.toDto(user);
  }
}
