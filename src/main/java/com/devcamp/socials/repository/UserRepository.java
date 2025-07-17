package com.devcamp.socials.repository;

import com.devcamp.socials.entity.RoleEntity;
import com.devcamp.socials.entity.UserEntity;
import java.sql.Date;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public Optional<UserEntity> findByUsername(String username) {
    final String SQL =
        """
        SELECT u.id, u.first_name, u.last_name, u.username, u.password, u.birth_date, u.enabled,
               r.id as role_id, r.name as role_name
        FROM users u
        LEFT JOIN user_roles ur ON u.id = ur.user_id
        LEFT JOIN roles r ON ur.role_id = r.id
        WHERE u.username = :username
        """;

    Map<String, Object> params = Map.of("username", username);

    return namedParameterJdbcTemplate.query(
        SQL,
        params,
        rs -> {
          UserEntity user = null;

          while (rs.next()) {
            if (user == null) {
              user =
                  UserEntity.builder()
                      .id(rs.getLong("id"))
                      .firstName(rs.getString("first_name"))
                      .lastName(rs.getString("last_name"))
                      .username(rs.getString("username"))
                      .password(rs.getString("password"))
                      .birthDate(
                          Optional.ofNullable(rs.getDate("birth_date"))
                              .map(Date::toLocalDate)
                              .orElse(null))
                      .enabled(rs.getBoolean("enabled"))
                      .roles(new ArrayList<>())
                      .build();
            }

            // Add a role if present
            Long roleId = rs.getLong("role_id");
            if (!rs.wasNull()) {
              RoleEntity role =
                  RoleEntity.builder().id(roleId).name(rs.getString("role_name")).build();
              user.getRoles().add(role);
            }
          }

          return Optional.ofNullable(user);
        });
  }
}
