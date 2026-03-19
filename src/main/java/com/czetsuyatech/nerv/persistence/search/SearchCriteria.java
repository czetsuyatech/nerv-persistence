package com.czetsuyatech.nerv.persistence.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a single parsed search condition.
 *
 * <p>A criteria entry consists of a field name, an operation symbol,
 * and a comparison value used to build a JPA {@code Specification}.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteria {

  /**
   * Target field or property name.
   */
  private String key;

  /**
   * Operator symbol used for comparison.
   */
  private String operation;

  /**
   * Comparison value associated with the operation.
   */
  private Object value;

  public String getKey() {
    return key;
  }

  public String getOperation() {
    return operation;
  }

  public Object getValue() {
    return value;
  }
}
