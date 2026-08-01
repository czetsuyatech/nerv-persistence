package com.czetsuyatech.nerv.persistence.specification;

import com.czetsuyatech.nerv.persistence.dto.UserModel;
import com.czetsuyatech.nerv.persistence.entity.UserEntity;
import com.czetsuyatech.nerv.persistence.entity.UserEntity_;
import com.czetsuyatech.nerv.persistence.specification.constant.RelationalOperators;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * Test builder for composing {@link Specification} instances for {@link UserEntity}.
 *
 * <p>The builder translates populated fields from {@link UserModel} into
 * specification predicates used by repository integration tests.
 */
public class UserSpecificationBuilder extends AbstractSpecificationsBuilder<UserEntity> {

  /**
   * Source DTO containing filter values.
   */
  private final UserModel userModel;

  /**
   * Flag controlling whether birth date nullability should be included in the query.
   */
  private Boolean nullBirthDate;

  /**
   * Creates a builder for the given DTO.
   *
   * @param userModel source DTO containing desired filter values
   */
  public UserSpecificationBuilder(final UserModel userModel) {
    this.userModel = userModel;
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
              RelationalOperators.NOTNULL.toString(), userModel.getBirthDate())));
    }

    if (userModel.getHobbies() != null && !userModel.getHobbies().isEmpty()) {
      spec = Specification.where(spec)
          .and(new GenericSpecification<>(new SearchCriteria(UserEntity_.hobbies.getName(),
              RelationalOperators.IN.toString(), userModel.getHobbies())));
    }

    if (StringUtils.hasLength(userModel.getFirstName())) {
      spec = Specification.where(spec)
          .and(new GenericSpecification<>(new SearchCriteria(UserEntity_.firstName.getName(),
              RelationalOperators.LIKE.toString(), userModel.getFirstName())));
    }

    if (StringUtils.hasLength(userModel.getLastName())) {
      spec = Specification.where(spec)
          .and(new GenericSpecification<>(new SearchCriteria(UserEntity_.lastName.getName(),
              RelationalOperators.EQUAL.toString(), userModel.getLastName())));
    }

    return spec;
  }
}
