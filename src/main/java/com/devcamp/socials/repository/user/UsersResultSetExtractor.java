package com.devcamp.socials.repository.user;

import com.devcamp.socials.entity.RoleEntity;
import com.devcamp.socials.entity.UserEntity;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import org.springframework.jdbc.core.ResultSetExtractor;

public class UsersResultSetExtractor implements ResultSetExtractor<List<UserEntity>> {

  @Override
  public List<UserEntity> extractData(ResultSet rs) throws SQLException {
    Map<Long, UserEntity> userMap = new LinkedHashMap<>();

    while (rs.next()) {
      Long userId = rs.getLong("id");

      UserEntity user = userMap.get(userId);
      if (user == null) {
        user =
            UserEntity.builder()
                .id(userId)
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

        userMap.put(userId, user);
      }

      Long roleId = rs.getLong("role_id");
      if (!rs.wasNull()) {
        RoleEntity role = RoleEntity.builder().id(roleId).name(rs.getString("role_name")).build();
        user.getRoles().add(role);
      }
    }

    return new ArrayList<>(userMap.values());
  }
}
