package com.devcamp.socials.repository.user;

import static com.devcamp.socials.repository.SqlConstant.*;

import com.devcamp.socials.entity.RoleEntity;
import com.devcamp.socials.entity.UserEntity;
import com.devcamp.socials.enums.MessageKey;
import com.devcamp.socials.exception.ApiException;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepository {
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public Optional<UserEntity> findByUsername(String username) {
    log.debug("Finding user by username: {}", username);

    try {
      UserEntity user =
          namedParameterJdbcTemplate.query(
              FIND_USER_BY_USERNAME_SQL,
              Map.of("username", username),
              new UserResultSetExtractor());

      return Optional.ofNullable(user);
    } catch (Exception e) {
      log.error("Error finding user by username: {}", username, e);
      return Optional.empty();
    }
  }

  public Optional<UserEntity> findById(Long id) {
    log.debug("Finding user by id: {}", id);

    try {
      UserEntity user =
          namedParameterJdbcTemplate.query(
              FIND_USER_BY_ID_SQL, Map.of("id", id), new UserResultSetExtractor());

      return Optional.ofNullable(user);
    } catch (Exception e) {
      log.error("Error finding user by id: {}", id, e);
      return Optional.empty();
    }
  }

  public Optional<RoleEntity> findRoleByName(String name) {
    log.debug("Finding role by name: {}", name);

    try {
      RoleEntity role =
          namedParameterJdbcTemplate.queryForObject(
              FIND_ROLE_BY_NAME_SQL, Map.of("name", name), new RoleRowMapper());
      return Optional.ofNullable(role);
    } catch (Exception e) {
      log.error("Error finding role by name: {}", name, e);
      return Optional.empty();
    }
  }

  @Transactional
  public void saveUser(UserEntity user) {
    log.debug("Saving user: {}", user.getUsername());

    MapSqlParameterSource userParams =
        new MapSqlParameterSource()
            .addValue("firstName", user.getFirstName())
            .addValue("lastName", user.getLastName())
            .addValue("username", user.getUsername())
            .addValue("password", user.getPassword())
            .addValue("birthDate", user.getBirthDate())
            .addValue("enabled", user.getEnabled());

    KeyHolder keyHolder = new GeneratedKeyHolder();
    namedParameterJdbcTemplate.update(INSERT_USER_SQL, userParams, keyHolder, new String[] {"id"});

    Number key = keyHolder.getKey();
    if (key == null) {
      throw new ApiException(MessageKey.INTERNAL_ERROR);
    }

    Long userId = key.longValue();

    if (user.getRoles() != null && !user.getRoles().isEmpty()) {
      List<MapSqlParameterSource> batchParams = new ArrayList<>();

      for (RoleEntity role : user.getRoles()) {
        MapSqlParameterSource roleParams =
            new MapSqlParameterSource().addValue("userId", userId).addValue("roleId", role.getId());
        batchParams.add(roleParams);
      }

      namedParameterJdbcTemplate.batchUpdate(
          INSERT_USER_ROLE_SQL, batchParams.toArray(new MapSqlParameterSource[0]));
    }
  }
}
