package com.devcamp.socials.handler;

import com.devcamp.socials.dto.LocalizedMessage;
import com.devcamp.socials.dto.Response;
import com.devcamp.socials.exception.ApiException;
import com.devcamp.socials.service.MessageLocalizer;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ExHandler {
  private final MessageLocalizer messageLocalizer;

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<Response<LocalizedMessage>> handleApiException(
      ApiException ex, HttpServletRequest request) {
    Locale locale = request.getLocale();
    LocalizedMessage localizedMessage =
        messageLocalizer.localize(ex.getMessageKey(), locale, ex.getArgs());

    log.warn("Handled ApiException: {}", localizedMessage.message());
    return ResponseEntity.status(localizedMessage.sc())
        .body(Response.failure(List.of(localizedMessage)));
  }
}
