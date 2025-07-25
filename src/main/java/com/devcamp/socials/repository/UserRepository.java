package com.devcamp.socials.repository;

import static com.devcamp.socials.repository.SqlConstant.*;

import com.devcamp.socials.dto.UserProfileResponse;
import com.devcamp.socials.entity.RoleEntity;
import com.devcamp.socials.entity.UserEntity;
import com.devcamp.socials.enums.MessageKey;
import com.devcamp.socials.exception.ApiException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepository {
  private final JdbcTemplate jdbcTemplate;

  public UserEntity findByUsername(String username) {
    log.debug("Finding user by username: {}", username);

    try {
      return jdbcTemplate.query(
          FIND_USER_BY_USERNAME_SQL,
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
                user.getRoles()
                    .add(RoleEntity.builder().id(roleId).name(rs.getString("role_name")).build());
              }
            }

            return user;
          },
          username);

    } catch (DataAccessException e) {
      log.error("Database error finding user by username: {}", username, e);
      return null;
    }
  }

  public RoleEntity findRoleByName(String name) {
    log.debug("Finding role by name: {}", name);

    try {
      return jdbcTemplate.queryForObject(
          FIND_ROLE_BY_NAME_SQL,
          (rs, rowNum) ->
              RoleEntity.builder().id(rs.getLong("id")).name(rs.getString("name")).build(),
          name);

    } catch (EmptyResultDataAccessException e) {
      log.error("No role found with name: {}", name);
      return null;
    } catch (DataAccessException e) {
      log.error("Database error finding role by name: {}", name, e);
      return null;
    }
  }

  @Transactional
  public void saveUser(UserEntity user) {
    log.debug("Saving user: {}", user.getUsername());

    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(
        connection -> {
          PreparedStatement ps =
              connection.prepareStatement(INSERT_USER_SQL, Statement.RETURN_GENERATED_KEYS);
          ps.setString(1, user.getFirstName());
          ps.setString(2, user.getLastName());
          ps.setString(3, user.getUsername());
          ps.setString(4, user.getPassword());
          ps.setDate(5, user.getBirthDate() != null ? Date.valueOf(user.getBirthDate()) : null);
          ps.setBoolean(6, user.getEnabled());
          return ps;
        },
        keyHolder);

    long userId = Objects.requireNonNull(keyHolder.getKey()).longValue();

    if (user.getRoles() != null && !user.getRoles().isEmpty()) {
      jdbcTemplate.batchUpdate(
          INSERT_USER_ROLE_SQL,
          user.getRoles(),
          user.getRoles().size(),
          (ps, role) -> {
            ps.setLong(1, userId);
            ps.setLong(2, role.getId());
          });
    }
  }

  public UserProfileResponse getProfileById(Long id) {
    log.debug("Finding user by id: {}", id);

    try {
      return jdbcTemplate.queryForObject(
          FIND_USER_BY_ID_SQL,
          (rs, rowNum) ->
              UserProfileResponse.builder()
                  .id(rs.getLong("id"))
                  .username(rs.getString("username"))
                  .firstName(rs.getString("first_name"))
                  .lastName(rs.getString("last_name"))
                  .birthDate(
                      rs.getDate("birth_date") != null
                          ? rs.getDate("birth_date").toLocalDate()
                          : null)
                  .build(),
          id);
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
      return jdbcTemplate.query(
          SEARCH_USERS_SQL,
          (rs, rowNum) ->
              UserProfileResponse.builder()
                  .id(rs.getLong("id"))
                  .username(rs.getString("username"))
                  .firstName(rs.getString("first_name"))
                  .lastName(rs.getString("last_name"))
                  .birthDate(
                      rs.getDate("birth_date") != null
                          ? rs.getDate("birth_date").toLocalDate()
                          : null)
                  .build(),
          firstName + "%",
          lastName + "%");
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
