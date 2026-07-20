package com.czetsuyatech.nerv.persistence.specification;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

/**
 * Base builder for creating a {@link Specification} from a collection of {@link SearchCriteria}.
 *
 * <p>This class provides common support for accumulating specification criteria and
 * combining them into a single specification using logical {@code and()} operations.
 *
 * @param <T> the entity type
 */
public abstract class AbstractSpecificationsBuilder<T> {

  /**
   * Accumulated specification criteria.
   */
  private final List<SearchCriteria> params = new ArrayList<>();

  /**
   * Builds a composed specification from all collected criteria.
   *
   * @return the composed specification
   */
  public Specification<T> build() {

    Specification<T> result = Specification.unrestricted();

    for (SearchCriteria param : params) {
      result = Specification.where(result).and(new GenericSpecification<>(param));
    }

    return result;
  }

  /**
   * Adds a specification criterion to the builder.
   *
   * @param key field name
   * @param operation operator symbol
   * @param value comparison value
   */
  protected void with(final String key, final String operation, final Object value) {
    params.add(new SearchCriteria(key, operation, value));
  }
}
