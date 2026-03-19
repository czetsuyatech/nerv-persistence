package com.czetsuyatech.nerv.persistence.entity;

/**
 * Contract for domain objects that expose a business code.
 */
public interface Code {

  /**
   * Returns the business code of this object.
   *
   * @return the business code
   */
  String getCode();

  /**
   * Sets the business code of this object.
   *
   * @param code the business code to assign
   */
  void setCode(String code);
}
