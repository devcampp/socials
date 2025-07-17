package com.devcamp.socials.dto;

import java.util.List;

public record Response<T>(T data, List<Error> errors) {
  public record Error(String title, String message) {}

  public static <T> Response<T> success(T data) {
    return new Response<>(data, null);
  }

  public static <T> Response<T> failure(List<LocalizedMessage> localizedMessages) {
    return new Response<>(
        null, localizedMessages.stream().map(l -> new Error(l.title(), l.message())).toList());
  }
}
