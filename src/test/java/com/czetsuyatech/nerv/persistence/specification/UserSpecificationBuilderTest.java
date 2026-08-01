package com.czetsuyatech.nerv.persistence.specification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;

import com.czetsuyatech.nerv.persistence.config.NervDataJpaTest;
import com.czetsuyatech.nerv.persistence.dto.UserModel;
import com.czetsuyatech.nerv.persistence.repository.UserRepository;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

/**
 * Integration tests for {@link UserSpecificationBuilder}.
 */
@SpringBootTest(classes = {UserRepository.class})
@ActiveProfiles("tst")
@DirtiesContext(classMode = AFTER_CLASS)
@Sql(value = {"/sql/users.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@NervDataJpaTest
@Slf4j
public class UserSpecificationBuilderTest {

  @Autowired
  private UserRepository userRepository;

  /**
   * Verifies that an empty DTO produces an unrestricted specification and returns the first page of users.
   */
  @SneakyThrows
  @Test
  void build_shouldReturnUnrestricted_whenSearchTermIsEmpty() {

    UserSpecificationBuilder userSpecificationBuilder = new UserSpecificationBuilder(UserModel.builder()
        .build());
    var userSpec = userSpecificationBuilder.build();

    var result = userRepository.findAllSlice(userSpec, Pageable.ofSize(10));

    assertThat(result).isNotNull();
    assertThat(result).hasSize(10);
  }

  /**
   * Verifies that first name is matched using a like comparison.
   */
  @Test
  void build_shouldReturnUnrestricted_whenParamIsNull() {

    var userDTO = UserModel.builder()
        .firstName("Ed")
        .build();
    UserSpecificationBuilder userSpecificationBuilder = new UserSpecificationBuilder(userDTO);
    var userSpec = userSpecificationBuilder.build();

    var result = userRepository.findAllSlice(userSpec, Pageable.ofSize(10));

    assertThat(result).isNotNull();
    assertThat(result).hasSize(2);
  }

  /**
   * Verifies that last name is matched exactly and returns no records when only a partial value is supplied.
   */
  @Test
  void build_shouldReturnEmptySlice_whenLasNameIsNotMatched() {

    var userDTO = UserModel.builder()
        .lastName("Leg")
        .build();
    UserSpecificationBuilder userSpecificationBuilder = new UserSpecificationBuilder(userDTO);
    var userSpec = userSpecificationBuilder.build();

    var result = userRepository.findAllSlice(userSpec, Pageable.ofSize(10));

    assertThat(result).isNotNull();
    assertThat(result).hasSize(0);
  }

  /**
   * Verifies that combining first-name like matching and last-name exact matching narrows the result to one user.
   */
  @Test
  void build_shouldReturnUser_whenFirstAndLastNameMatched() {

    var userDTO = UserModel.builder()
        .firstName("Edward")
        .lastName("Legaspi")
        .build();
    UserSpecificationBuilder userSpecificationBuilder = new UserSpecificationBuilder(userDTO);
    var userSpec = userSpecificationBuilder.build();

    var result = userRepository.findAllSlice(userSpec, Pageable.ofSize(10));
    result.getContent().forEach(e -> System.out.println(e));

    assertThat(result).isNotNull();
    assertThat(result).hasSize(1);
  }

  /**
   * Verifies that users with non-null birth dates are returned when null birth dates are excluded.
   */
  @Test
  void build_shouldReturn1_whenDateIsNotNull() {

    UserModel userModel = UserModel.builder()
        .build();

    UserSpecificationBuilder userSpecificationBuilder = new UserSpecificationBuilder(userModel);
    userSpecificationBuilder.setNullBirthDate(false);
    var userSpec = userSpecificationBuilder.build();

    var result = userRepository.findAllSlice(userSpec, Pageable.ofSize(10));

    assertThat(result).isNotNull();
    assertThat(result).hasSize(2);
  }

  /**
   * Verifies that hobby membership filtering returns the expected users.
   */
  @Test
  void build_shouldReturn2_whenNameIsEdward() {

    UserModel userModel = UserModel.builder()
        .hobbies(List.of("Chess", "Anime"))
        .build();

    UserSpecificationBuilder userSpecificationBuilder = new UserSpecificationBuilder(userModel);
    var userSpec = userSpecificationBuilder.build();

    var result = userRepository.findAllSlice(userSpec, Pageable.ofSize(10));

    assertThat(result).isNotNull();
    assertThat(result).hasSize(2);
  }
}
