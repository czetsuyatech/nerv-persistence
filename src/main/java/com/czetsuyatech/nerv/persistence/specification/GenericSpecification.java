package com.czetsuyatech.nerv.persistence.specification;

import com.czetsuyatech.nerv.persistence.specification.constant.RelationalOperators;
import com.czetsuyatech.nerv.persistence.specification.constant.SpecificationConstant;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.Instant;
import java.util.Collection;
import org.springframework.data.jpa.domain.Specification;

/**
 * Generic {@link Specification} implementation backed by a single {@link SearchCriteria}.
 *
 * @param <T> the entity type
 */
public class GenericSpecification<T> implements Specification<T> {

  private static final long serialVersionUID = -2769484968147454259L;

  /**
   * Keyword describing an {@code IN} operation.
   */
  public static final String IN = "IN";

  /**
   * Keyword describing a join-based operation.
   */
  public static final String JOIN = "JOIN";

  /**
   * Keyword describing a not-null operation.
   */
  public static final String NOT_NULL = "NOT_NULL";

  /**
   * Search criteria represented by this specification.
   */
  private final SearchCriteria criteria;

  /**
   * Creates a new specification for the given specification criteria.
   *
   * @param searchCriteria the criteria to apply
   */
  public GenericSpecification(final SearchCriteria searchCriteria) {
    this.criteria = searchCriteria;
  }

  /**
   * Converts the configured {@link SearchCriteria} into a JPA {@link Predicate}.
   *
   * @param root query root
   * @param query criteria query
   * @param builder criteria builder
   * @return the generated predicate, or {@code null} if the operation is unsupported
   */
  @Override
  public Predicate toPredicate(final Root<T> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {

    String operation = criteria.getOperation();
    String key = criteria.getKey();
    Object value = criteria.getValue();

    if (RelationalOperators.GREATER_THAN_EQUAL.toString().equalsIgnoreCase(operation)) {
      return buildComparison(root, builder, builder::greaterThanOrEqualTo);
    }

    if (RelationalOperators.LESS_THAN_EQUAL.toString().equalsIgnoreCase(operation)) {
      return buildComparison(root, builder, builder::lessThanOrEqualTo);
    }

    if (RelationalOperators.EQUAL.toString().equalsIgnoreCase(operation)) {
      return builder.equal(root.get(key), value);
    }

    if (RelationalOperators.NOTEQUAL.toString().equalsIgnoreCase(operation)) {
      return builder.notEqual(root.get(key), value);
    }

    if (RelationalOperators.LIKE.toString().equalsIgnoreCase(operation)
        && root.get(key).getJavaType() == String.class) {
      return builder.like(root.get(key),
          SpecificationConstant.LIKE_WILDCARD + value + SpecificationConstant.LIKE_WILDCARD);
    }

    if (RelationalOperators.IN.toString().equalsIgnoreCase(operation)
        && value instanceof Collection<?> values) {
      if (root.getModel().getAttribute(key).isCollection()) {
        query.distinct(true);
        return root.join(key).in(values);
      }

      return root.get(key).in(values);
    }

    if (RelationalOperators.JOIN.toString().equalsIgnoreCase(operation)) {
      String[] split = key.split("\\.");
      return builder.equal(root.join(split[0]).get(split[1]), value);
    }

    if (RelationalOperators.NOTNULL.toString().equalsIgnoreCase(operation)) {
      return root.get(key).isNotNull();
    }

    return null;
  }

  /**
   * Builds a comparable predicate for fields that support ordering.
   *
   * @param root query root
   * @param builder criteria builder
   * @param comparisonBuilder comparison strategy
   * @param <Y> comparable field type
   * @return the generated predicate
   */
  @SuppressWarnings("unchecked")
  private <Y extends Comparable<? super Y>> Predicate buildComparison(
      Root<T> root,
      CriteriaBuilder builder,
      ComparisonBuilder<Y> comparisonBuilder) {

    if (root.get(criteria.getKey()).getJavaType() == Instant.class) {
      return comparisonBuilder.build(root.get(criteria.getKey()), (Y) criteria.getValue());
    }

    return comparisonBuilder.build(root.get(criteria.getKey()), (Y) criteria.getValue().toString());
  }

  /**
   * Internal strategy interface for building comparison predicates.
   *
   * @param <Y> comparable type
   */
  @FunctionalInterface
  private interface ComparisonBuilder<Y extends Comparable<? super Y>> {

    /**
     * Builds a predicate for the given expression and value.
     *
     * @param expression field expression
     * @param value comparison value
     * @return the generated predicate
     */
    Predicate build(Expression<? extends Y> expression, Y value);
  }
}
