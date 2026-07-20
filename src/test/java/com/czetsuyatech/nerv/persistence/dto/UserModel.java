package com.czetsuyatech.nerv.persistence.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * Simple test DTO used for projection and mapping scenarios.
 */
@Data
@Builder
public class UserModel {

  /**
   * User first name.
   */
  private String firstName;

  /**
   * User last name.
   */
  private String lastName;

  /**
   * User birth date and time.
   */
  private LocalDateTime birthDate;

  /**
   * User hobbies.
   */
  private List<String> hobbies;
}
