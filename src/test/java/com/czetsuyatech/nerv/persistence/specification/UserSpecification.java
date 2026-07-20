package com.czetsuyatech.nerv.persistence.specification;

import com.czetsuyatech.nerv.persistence.entity.UserEntity;
import com.czetsuyatech.nerv.persistence.specification.constant.RelationalOperators;
import com.czetsuyatech.nerv.persistence.specification.constant.SpecificationConstant;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

/**
 * Test-specific {@link Specification} implementation for filtering {@link UserEntity} records.
 *
 * <p>This specification supports a subset of operators used by the tests,
 * currently focusing on exact and like-based matching for whitelisted user fields.
 */
@AllArgsConstructor
public class UserSpecification implements Specification<UserEntity> {

  private static final long serialVersionUID = 1L;

  /**
   * Search criteria represented by this specification.
   */
  private final transient SearchCriteria criteria;

  /**
   * Converts the configured {@link SearchCriteria} into a JPA {@link Predicate}.
   *
   * @param root query root
   * @param query criteria query
   * @param criteriaBuilder criteria builder
   * @return the generated predicate, or {@code null} if the operator is unsupported
   */
  @Override
  @SuppressWarnings("unchecked")
  public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

    var key = UserSearchFields.valueOf(criteria.getKey().toUpperCase());
    RelationalOperators operator = RelationalOperators.getOperator(criteria.getOperation());
    Object value = criteria.getValue();
    Expression<?> expression = root.<String>get(key.toString());

    Predicate predicate = null;
    switch (operator) {
      case EQUAL -> predicate = criteriaBuilder.equal(expression, value);
      case LIKE -> predicate = criteriaBuilder.like(criteriaBuilder.upper((Expression<String>) expression),
          SpecificationConstant.LIKE_WILDCARD + value.toString().toUpperCase() + SpecificationConstant.LIKE_WILDCARD);
    }

    return predicate;
  }
}
