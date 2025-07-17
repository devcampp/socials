package com.devcamp.socials.dto;

import java.util.List;

public record Response<T>(T data, List<Error> errors) {
  public record Error(String title, String message) {}
}
