package com.devcamp.socials.repository;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SqlConstant {
  public static final String FIND_USER_BY_USERNAME_SQL =
      """
        SELECT u.id, u.first_name, u.last_name, u.username, u.password, u.birth_date, u.enabled,
               r.id as role_id, r.name as role_name
        FROM users u
        LEFT JOIN user_roles ur ON u.id = ur.user_id
        LEFT JOIN roles r ON ur.role_id = r.id
        WHERE u.username = :username
        """;

  public static final String SEARCH_USERS_SQL =
      """
      SELECT u.id, u.first_name, u.last_name, u.username, u.password, u.birth_date, u.enabled,
             r.id as role_id, r.name as role_name
      FROM users u
      LEFT JOIN user_roles ur ON u.id = ur.user_id
      LEFT JOIN roles r ON ur.role_id = r.id
      WHERE u.first_name LIKE :firstName AND
            u.last_name LIKE :lastName
      ORDER BY u.id ASC
      """;

  public static final String FIND_USER_BY_ID_SQL =
      """
        SELECT u.id, u.first_name, u.last_name, u.username, u.password, u.birth_date, u.enabled,
               r.id as role_id, r.name as role_name
        FROM users u
        LEFT JOIN user_roles ur ON u.id = ur.user_id
        LEFT JOIN roles r ON ur.role_id = r.id
        WHERE u.id = :id
        """;

  public static final String INSERT_USER_SQL =
      """
        INSERT INTO users (first_name, last_name, username, password, birth_date, enabled)
        VALUES (:firstName, :lastName, :username, :password, :birthDate, :enabled)
        """;

  public static final String INSERT_USER_ROLE_SQL =
      """
        INSERT INTO user_roles (user_id, role_id) VALUES (:userId, :roleId)
        """;

  public static final String FIND_ROLE_BY_NAME_SQL =
      """
        SELECT id, name
        FROM roles
        WHERE name = :name
        """;
}
