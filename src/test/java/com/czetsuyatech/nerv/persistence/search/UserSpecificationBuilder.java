package com.czetsuyatech.nerv.persistence.search;

import com.czetsuyatech.nerv.persistence.dtos.UserDTO;
import com.czetsuyatech.nerv.persistence.entity.UserEntity;
import com.czetsuyatech.nerv.persistence.entity.UserEntity_;
import com.czetsuyatech.nerv.persistence.search.constant.RelationalOperators;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * Test builder for composing {@link Specification} instances for {@link UserEntity}.
 *
 * <p>The builder translates populated fields from {@link UserDTO} into
 * specification predicates used by repository integration tests.
 */
public class UserSpecificationBuilder extends AbstractSpecificationsBuilder<UserEntity> {

  /**
   * Source DTO containing filter values.
   */
  private final UserDTO userDTO;

  /**
   * Flag controlling whether birth date nullability should be included in the query.
   */
  private Boolean nullBirthDate;

  /**
   * Creates a builder for the given DTO.
   *
   * @param userDTO source DTO containing desired filter values
   */
  public UserSpecificationBuilder(final UserDTO userDTO) {
    this.userDTO = userDTO;
  }

  /**
   * Sets whether birth date nullability should be considered in the generated specification.
   *
   * @param nullBirthDate flag indicating null-birth-date handling
   */
  public void setNullBirthDate(Boolean nullBirthDate) {
    this.nullBirthDate = nullBirthDate;
  }

  /**
   * Builds a specification from the configured DTO fields and flags.
   *
   * @return the composed user specification
   */
  @Override
  public Specification<UserEntity> build() {

    Specification<UserEntity> spec = Specification.unrestricted();

    if (nullBirthDate != null && !nullBirthDate) {
      spec = Specification.where(spec)
          .and(new GenericSpecification<>(new SearchCriteria(UserEntity_.birthDate.getName(),
              RelationalOperators.NOTNULL.toString(), userDTO.getBirthDate())));
    }

    if (userDTO.getHobbies() != null && !userDTO.getHobbies().isEmpty()) {
      spec = Specification.where(spec)
          .and(new GenericSpecification<>(new SearchCriteria(UserEntity_.hobbies.getName(),
              RelationalOperators.IN.toString(), userDTO.getHobbies())));
    }

    if (StringUtils.hasLength(userDTO.getFirstName())) {
      spec = Specification.where(spec)
          .and(new GenericSpecification<>(new SearchCriteria(UserEntity_.firstName.getName(),
              RelationalOperators.LIKE.toString(), userDTO.getFirstName())));
    }

    if (StringUtils.hasLength(userDTO.getLastName())) {
      spec = Specification.where(spec)
          .and(new GenericSpecification<>(new SearchCriteria(UserEntity_.lastName.getName(),
              RelationalOperators.EQUAL.toString(), userDTO.getLastName())));
    }

    return spec;
  }
}
