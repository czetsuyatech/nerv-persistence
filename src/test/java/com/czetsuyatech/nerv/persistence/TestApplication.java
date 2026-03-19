package com.czetsuyatech.nerv.persistence;

import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Boot application used for repository and persistence integration tests.
 */
@SpringBootApplication
public class TestApplication {

  /**
   * Utility class constructor.
   */
  private TestApplication() {
  }

  /**
   * Starts the test application using UTC as the default JVM time zone.
   *
   * @param args application arguments
   */
  public static void main(String[] args) {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    SpringApplication.run(TestApplication.class, args);
  }
}
