package com.devcamp.socials.repository.user;

import com.devcamp.socials.entity.RoleEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class RoleRowMapper implements RowMapper<RoleEntity> {
  @Override
  public RoleEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    return RoleEntity.builder().id(rs.getLong("id")).name(rs.getString("name")).build();
  }
}
