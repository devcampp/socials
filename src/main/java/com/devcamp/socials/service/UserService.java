package com.devcamp.socials.service;

import com.devcamp.socials.dto.SignUpRequest;
import com.devcamp.socials.entity.RoleEntity;
import com.devcamp.socials.entity.UserEntity;
import com.devcamp.socials.enums.MessageKey;
import com.devcamp.socials.exception.ApiException;
import com.devcamp.socials.mapper.UserMapper;
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

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity user = userRepository.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException(username);
    }
    return user;
  }

  public void createUser(SignUpRequest request, String encodedPassword) {
    RoleEntity role = userRepository.findRoleByName("USER");

    if (role == null) {
      log.error("Internal error, role not found");
      throw new ApiException(MessageKey.INTERNAL_ERROR);
    }

    UserEntity user = UserMapper.INSTANCE.toEntity(request, encodedPassword, List.of(role));
    userRepository.saveUser(user);
  }
}
