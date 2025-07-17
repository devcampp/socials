package com.devcamp.socials.repository;

import com.devcamp.socials.entity.RoleEntity;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoleRepository {
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public Optional<RoleEntity> findByName(String name) {
    final String SQL =
        """
          SELECT id, name
          FROM roles
          WHERE name = :name
          """;

    Map<String, Object> params = Map.of("name", name);

    return namedParameterJdbcTemplate.query(
        SQL,
        params,
        rs -> {
          RoleEntity role = null;

          while (rs.next()) {
            if (role == null) {
              role = RoleEntity.builder().id(rs.getLong("id")).name(rs.getString("name")).build();
            }
          }

          return Optional.ofNullable(role);
        });
  }
}
