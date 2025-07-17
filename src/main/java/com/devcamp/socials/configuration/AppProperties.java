package com.devcamp.socials.configuration;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppProperties(List<String> whiteList, Jwt jwt) {
  public record Jwt(String secret, Long expiration) {}
}
