package com.czetsuyatech.nerv.persistence.search.constant;

/**
 * Supported relational operators used when parsing search expressions.
 *
 * <p>Each enum constant maps to the symbolic representation expected by
 * the query parsing layer.
 */
public enum RelationalOperators {

  /**
   * Greater-than operator ({@code >}).
   */

  GREATER {
    @Override
    public String toString() {
      return ">";
    }
  },
  LESS {
    @Override
    public String toString() {
      return "<";
    }
  },
  EQUAL {
    @Override
    public String toString() {
      return ":";
    }
  },
  NOTEQUAL {
    @Override
    public String toString() {
      return "/";
    }
  },
  ISNULL {
    @Override
    public String toString() {
      return "~";
    }
  },
  GREATER_THAN_EQUAL {
    @Override
    public String toString() {
      return ">=";
    }
  },
  LESS_THAN_EQUAL {
    @Override
    public String toString() {
      return "<=";
    }
  },
  LIKE {
    @Override
    public String toString() {
      return "*";
    }
  },
  IN {
    @Override
    public String toString() {
      return "@";
    }
  },
  JOIN {
    @Override
    public String toString() {
      return "^";
    }
  },
  NOTNULL {
    @Override
    public String toString() {
      return "!";
    }
  };

  /**
   * Resolves an operator enum from its symbolic value.
   *
   * @param value the symbolic operator representation
   * @return the matching relational operator
   * @throws IllegalArgumentException if no operator matches the supplied value
   */
  public static RelationalOperators getOperator(String value) {
    for (RelationalOperators operator : values()) {
      if (operator.toString().equals(value)) {
        return operator;
      }
    }

    throw new IllegalArgumentException(String.format("Operator %s not found.", value));
  }
}
