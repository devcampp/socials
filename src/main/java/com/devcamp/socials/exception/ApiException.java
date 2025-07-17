package com.devcamp.socials.exception;

import com.devcamp.socials.enums.MessageKey;
import java.util.Arrays;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
  private final MessageKey messageKey;
  private final Object[] args;

  public ApiException(MessageKey messageKey) {
    this(messageKey, null, null);
  }

  public ApiException(MessageKey messageKey, Object... args) {
    this(messageKey, null, args);
  }

  public ApiException(MessageKey messageKey, Throwable cause, Object[] args) {
    super("Exception key: " + messageKey + " | Args: " + Arrays.toString(args), cause);
    this.messageKey = messageKey;
    this.args = args;
  }
}
