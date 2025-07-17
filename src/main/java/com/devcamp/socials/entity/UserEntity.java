package com.devcamp.socials.entity;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Builder
public class UserEntity implements UserDetails {
  private Long id;
  private String firstName;
  private String lastName;
  private String username;
  private String password;
  private LocalDate birthDate;
  private boolean enabled;
  private List<RoleEntity> roles;

  @Override
  public String getUsername() {
    return this.username;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream()
        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
        .toList();
  }

  @Override
  public boolean isEnabled() {
    return this.enabled;
  }
}
