package com.devcamp.socials.service;

import java.util.Locale;

import com.devcamp.socials.dto.LocalizedMessage;
import com.devcamp.socials.enums.MessageKey;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageLocalizer {
  private final MessageSource messageSource;

  public LocalizedMessage localize(MessageKey key, Locale locale, Object... args) {
    String scKey = key.getBaseKey() + ".sc";
    String titleKey = key.getBaseKey() + ".title";
    String messageKey = key.getBaseKey() + ".message";

    int sc = Integer.parseInt(resolve(scKey, locale, args));
    String title = resolve(titleKey, locale, args);
    String message = resolve(messageKey, locale, args);

    return new LocalizedMessage(sc, title, message);
  }

  private String resolve(String key, Locale locale, Object... args) {
    return messageSource.getMessage(key, args, locale);
  }
}
