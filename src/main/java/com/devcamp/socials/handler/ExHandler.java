package com.devcamp.socials.handler;

import com.devcamp.socials.dto.LocalizedMessage;
import com.devcamp.socials.dto.Response;
import com.devcamp.socials.enums.MessageKey;
import com.devcamp.socials.exception.ApiException;
import com.devcamp.socials.service.MessageLocalizer;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ExHandler {
  private final MessageLocalizer messageLocalizer;

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<Response<Void>> handleApiException(ApiException ex, Locale locale) {
    LocalizedMessage localizedMessage =
        messageLocalizer.localize(ex.getMessageKey(), locale, ex.getArgs());

    log.debug("Handled ApiException and returned: {}", localizedMessage.message());
    return ResponseEntity.status(localizedMessage.sc())
        .body(Response.failure(List.of(localizedMessage)));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Response<Void>> handleValidationExceptions(
      MethodArgumentNotValidException ex, Locale locale) {

    List<LocalizedMessage> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(
                fieldError -> {
                  String messageKey = fieldError.getDefaultMessage();
                  String fieldName = fieldError.getField();
                  MessageKey key = MessageKey.valueOf(messageKey);
                  return messageLocalizer.localize(key, locale, fieldName);
                })
            .toList();

    return ResponseEntity.badRequest().body(Response.failure(errors));
  }
}
