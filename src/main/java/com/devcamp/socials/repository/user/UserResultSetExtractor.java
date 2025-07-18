package com.devcamp.socials.repository.user;

import com.devcamp.socials.entity.RoleEntity;
import com.devcamp.socials.entity.UserEntity;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.jdbc.core.ResultSetExtractor;

public class UserResultSetExtractor implements ResultSetExtractor<UserEntity> {
  @Override
  public UserEntity extractData(ResultSet rs) throws SQLException {
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
        RoleEntity role = RoleEntity.builder().id(roleId).name(rs.getString("role_name")).build();
        user.getRoles().add(role);
      }
    }

    return user;
  }
}
