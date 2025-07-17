package com.devcamp.socials.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageKey {
  BAD_CREDENTIALS("auth.bad_credentials"),
  USER_DISABLED("auth.user_disabled"),
  MISSING_REQUIRED_FIELDS("validation.missing_required_fields");

  private final String baseKey;
}
