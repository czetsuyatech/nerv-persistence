package com.czetsuyatech.nerv.persistence.entity;

/**
 * Contract for objects that provide sorting metadata.
 */
public interface ISortable {

  /**
   * Returns the field or property name used for sorting.
   *
   * @return the sort field name
   */
  String getSortOrderBy();

  /**
   * Returns the sort order position or priority.
   *
   * @return the sort order value
   */
  Integer getSortOrder();
}
