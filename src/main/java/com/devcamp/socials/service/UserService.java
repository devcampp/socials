package com.devcamp.socials.service;

import com.devcamp.socials.dto.SignUpRequest;
import com.devcamp.socials.dto.UserResponse;
import com.devcamp.socials.entity.RoleEntity;
import com.devcamp.socials.entity.UserEntity;
import com.devcamp.socials.enums.MessageKey;
import com.devcamp.socials.exception.ApiException;
import com.devcamp.socials.mapper.UserMapper;
import com.devcamp.socials.repository.RoleRepository;
import com.devcamp.socials.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository
        .findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(username));
  }

  public UserResponse getUserRepository(Long id) {
    UserEntity user =
        this.userRepository
            .findById(id)
            .orElseThrow(() -> new ApiException(MessageKey.USER_NOT_FOUND, "id: " + id));

    return UserMapper.INSTANCE.toDto(user);
  }

  public void createUser(SignUpRequest request, String encodedPassword) {
    RoleEntity roleEntity =
        roleRepository
            .findByName("USER")
            .orElseThrow(
                () -> {
                  log.error("Internal error, role not found");
                  return new ApiException(MessageKey.INTERNAL_ERROR);
                });

    UserEntity user = UserMapper.INSTANCE.toEntity(request, encodedPassword, List.of(roleEntity));
    userRepository.save(user);
  }
}
