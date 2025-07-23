package com.devcamp.socials.repository;

import static com.devcamp.socials.repository.SqlConstant.*;

import com.devcamp.socials.dto.UserProfileResponse;
import com.devcamp.socials.entity.RoleEntity;
import com.devcamp.socials.entity.UserEntity;
import com.devcamp.socials.enums.MessageKey;
import com.devcamp.socials.exception.ApiException;
import java.sql.Date;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
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

  public UserEntity findByUsername(String username) {
    log.debug("Finding user by username: {}", username);

    try {
      return namedParameterJdbcTemplate.query(
          FIND_USER_BY_USERNAME_SQL,
          Map.of("username", username),
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

              Long roleId = rs.getLong("role_id");
              if (!rs.wasNull()) {
                RoleEntity role =
                    RoleEntity.builder().id(roleId).name(rs.getString("role_name")).build();
                user.getRoles().add(role);
              }
            }

            return user;
          });

    } catch (DataAccessException e) {
      log.error("Database error finding user by username: {}", username, e);
      return null;
    }
  }

  public RoleEntity findRoleByName(String name) {
    log.debug("Finding role by name: {}", name);

    try {
      return namedParameterJdbcTemplate.queryForObject(
          FIND_ROLE_BY_NAME_SQL,
          Map.of("name", name),
          (rs, rowNum) ->
              RoleEntity.builder().id(rs.getLong("id")).name(rs.getString("name")).build());

    } catch (EmptyResultDataAccessException e) {
      log.debug("No role found with name: {}", name);
      return null;
    } catch (DataAccessException e) {
      log.error("Database error finding role by name: {}", name, e);
      return null;
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

  public UserProfileResponse getProfileById(Long id) {
    log.debug("Finding user by id: {}", id);

    try {
      return namedParameterJdbcTemplate.queryForObject(
          FIND_USER_BY_ID_SQL,
          Map.of("id", id),
          (rs, rowNum) ->
              UserProfileResponse.builder()
                  .id(rs.getLong("id"))
                  .username(rs.getString("username"))
                  .firstName(rs.getString("first_name"))
                  .lastName(rs.getString("last_name"))
                  .birthDate(
                      Optional.ofNullable(rs.getDate("birth_date"))
                          .map(Date::toLocalDate)
                          .orElse(null))
                  .build());
    } catch (EmptyResultDataAccessException e) {
      log.debug("No user found with id: {}", id);
      return null;
    } catch (DataAccessException e) {
      log.error("Database error finding user by id: {}", id, e);
      throw new ApiException(MessageKey.INTERNAL_ERROR);
    }
  }

  public List<UserProfileResponse> searchProfiles(String firstName, String lastName) {
    log.debug("Getting users by first name: {} and last name: {}", firstName, lastName);

    try {
      MapSqlParameterSource params =
          new MapSqlParameterSource()
              .addValue("firstName", firstName + "%")
              .addValue("lastName", lastName + "%");

      return namedParameterJdbcTemplate.query(
          SEARCH_USERS_SQL,
          params,
          rs -> {
            List<UserProfileResponse> users = new ArrayList<>();
            while (rs.next()) {
              users.add(
                  UserProfileResponse.builder()
                      .id(rs.getLong("id"))
                      .username(rs.getString("username"))
                      .firstName(rs.getString("first_name"))
                      .lastName(rs.getString("last_name"))
                      .birthDate(
                          Optional.ofNullable(rs.getDate("birth_date"))
                              .map(Date::toLocalDate)
                              .orElse(null))
                      .build());
            }

            return users;
          });
    } catch (DataAccessException e) {
      log.error(
          "Database error searching users by first name: {} and last name: {}",
          firstName,
          lastName,
          e);
      throw new ApiException(MessageKey.INTERNAL_ERROR);
    }
  }
}
