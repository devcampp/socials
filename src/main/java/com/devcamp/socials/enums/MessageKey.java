package com.devcamp.socials.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageKey {
  INTERNAL_ERROR("internal_error"),
  BAD_CREDENTIALS("bad_credentials"),
  USER_DISABLED("user_disabled"),
  USER_NOT_FOUND("user_not_found"),
  MISSING_REQUIRED_FIELDS("missing_required_fields");

  private final String baseKey;
}
