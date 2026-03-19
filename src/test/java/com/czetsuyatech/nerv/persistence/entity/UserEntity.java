package com.czetsuyatech.nerv.persistence.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Test entity representing a user account.
 */
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
@Entity
@Table(name = "user_account")
@SequenceGenerator(name = "nerv_sequence_generator", sequenceName = "user_seq", allocationSize = 1)
public class UserEntity extends BaseEntity {

  /**
   * User first name.
   */
  @Column(name = "first_name")
  private String firstName;

  /**
   * User last name.
   */
  @Column(name = "last_name")
  private String lastName;

  /**
   * User birth date.
   */
  @Column(name = "birth_date")
  private LocalDateTime birthDate;

  /**
   * Collection of user hobbies.
   */
  @ElementCollection
  @CollectionTable(
      name = "user_hobby",
      joinColumns = @JoinColumn(name = "user_id")
  )
  @Column(name = "hobby")
  private List<String> hobbies;
}
