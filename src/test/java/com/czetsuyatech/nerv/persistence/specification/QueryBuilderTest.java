package com.czetsuyatech.nerv.persistence.specification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;

import com.czetsuyatech.nerv.persistence.config.NervDataJpaTest;
import com.czetsuyatech.nerv.persistence.entity.UserEntity;
import com.czetsuyatech.nerv.persistence.repository.UserRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

/**
 * Integration tests for {@link QueryBuilder}.
 */
@SpringBootTest(classes = {UserRepository.class})
@ActiveProfiles("tst")
@DirtiesContext(classMode = AFTER_CLASS)
@Sql(value = {"/sql/users.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@NervDataJpaTest
@Slf4j
class QueryBuilderTest {

  @Autowired
  private UserRepository userRepository;

  /**
   * Verifies that an empty specification string produces an unrestricted specification.
   */
  @SneakyThrows
  @Test
  void build_shouldReturnUnrestricted_whenSearchTermIsEmpty() {

    var userSpec = QueryBuilder.<UserEntity, UserSpecification>build("", UserSpecification.class,
        UserSearchFields.class);

    assertThat(userSpec).isEqualTo(Specification.unrestricted());
  }

  /**
   * Verifies that invalid operators produce no parsed criteria and therefore return an unrestricted specification.
   */
  @SneakyThrows
  @Test
  void build_shouldReturnUnrestricted_whenParamIsNull() {

    var searchParams = "firstNamexEd";
    var userSpec = QueryBuilder.<UserEntity, UserSpecification>build(searchParams, UserSpecification.class,
        UserSearchFields.class);

    assertThat(userSpec).isEqualTo(Specification.unrestricted());
  }

  /**
   * Verifies that exact matching does not return partial-name matches.
   */
  @SneakyThrows
  @Test
  void build_shouldReturnEmptySlice_whenFirstNameIsNotMatched() {

    var searchParams = "firstName:Ed";
    var userSpec = QueryBuilder.<UserEntity, UserSpecification>build(searchParams, UserSpecification.class,
        UserSearchFields.class);

    var result = userRepository.findAllSlice(userSpec, Pageable.ofSize(10));

    assertThat(result).isNotNull();
    assertThat(result).hasSize(0);
  }

  /**
   * Verifies that an exact first-name match returns the expected users.
   */
  @SneakyThrows
  @Test
  void build_shouldReturnUser_whenFirstNameIsMatched() {

    var searchParams = "firstName:Edward";
    var userSpec = QueryBuilder.<UserEntity, UserSpecification>build(searchParams, UserSpecification.class,
        UserSearchFields.class);

    var result = userRepository.findAllSlice(userSpec, Pageable.ofSize(10));

    assertThat(result).isNotNull();
    assertThat(result).hasSize(2);
    assertThat(result.get().findFirst().get().getLastName()).isEqualTo("Legaspi");
  }

  /**
   * Verifies that combining first-name and last-name criteria narrows the result to a single user.
   */
  @SneakyThrows
  @Test
  void build_shouldReturnUser_whenFirstAndLastNameMatched() {

    var searchParams = "firstName:Edward,lastName:Legaspi";
    var userSpec = QueryBuilder.<UserEntity, UserSpecification>build(searchParams, UserSpecification.class,
        UserSearchFields.class);

    var result = userRepository.findAllSlice(userSpec, Pageable.ofSize(10));

    assertThat(result).isNotNull();
    assertThat(result).hasSize(1);
    assertThat(result.get().findFirst().get().getFirstName()).isEqualTo("Edward");
    assertThat(result.get().findFirst().get().getLastName()).isEqualTo("Legaspi");
  }

  /**
   * Verifies that a like specification on first name returns all matching users.
   */
  @SneakyThrows
  @Test
  void build_shouldReturnUser_whenFirstNameLikeIsMatched() {

    var searchParams = "firstName*ar";
    var userSpec = QueryBuilder.<UserEntity, UserSpecification>build(searchParams, UserSpecification.class,
        UserSearchFields.class);

    var result = userRepository.findAllSlice(userSpec, Pageable.ofSize(10));

    assertThat(result).isNotNull();
    assertThat(result).hasSize(5);
    assertThat(result.get().findFirst().get().getFirstName()).isEqualTo("Edward");
    assertThat(result.get().findFirst().get().getLastName()).isEqualTo("Legaspi");
  }

  /**
   * Verifies that a like specification on last name returns all expected matches.
   */
  @SneakyThrows
  @Test
  void build_shouldReturnUser_whenLastNameLikeIsMatched() {

    var searchParams = "lastName*ar";
    var userSpec = QueryBuilder.<UserEntity, UserSpecification>build(searchParams, UserSpecification.class,
        UserSearchFields.class);

    var result = userRepository.findAllSlice(userSpec, Pageable.ofSize(10));

    assertThat(result).isNotNull();
    assertThat(result).hasSize(3);
    assertThat(result.get().findFirst().get().getFirstName()).isEqualTo("Frank");
    assertThat(result.get().findFirst().get().getLastName()).isEqualTo("Garcia");
  }
}
