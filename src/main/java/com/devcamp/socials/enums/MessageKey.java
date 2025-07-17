package com.devcamp.socials.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageKey {
  MISSING_REQUIRED_FIELDS("validation.missing_required_fields");

  private final String baseKey;
}
