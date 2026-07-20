package com.czetsuyatech.nerv.persistence.specification.constant;

/**
 * Constants used by the specification and query-building infrastructure.
 */
public class SpecificationConstant {

  /**
   * Utility class constructor.
   */
  private SpecificationConstant() {

  }

  /**
   * String value used to represent {@code null} in specification expressions.
   */
  public static final String NULL_VALUE = "Null";

  /**
   * SQL wildcard used for like-based searches.
   */
  public static final String LIKE_WILDCARD = "%";

  /**
   * Separator used for logical AND composition in specification strings.
   */
  public static final String AND_HASH = "#";
}
