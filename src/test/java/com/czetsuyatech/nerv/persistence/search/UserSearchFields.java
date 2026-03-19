package com.czetsuyatech.nerv.persistence.search;

/**
 * Whitelist of searchable user fields used by query parsing tests.
 */
public enum UserSearchFields {

  /**
   * First-name field.
   */
  FIRSTNAME {
    @Override
    public String toString() {
      return "firstName";
    }
  },

  /**
   * Last-name field.
   */
  LASTNAME {
    @Override
    public String toString() {
      return "lastName";
    }
  }
}
