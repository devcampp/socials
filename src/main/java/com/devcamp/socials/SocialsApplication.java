package com.devcamp.socials;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SocialsApplication {

  public static void main(String[] args) {
    SpringApplication.run(SocialsApplication.class, args);
  }

}
