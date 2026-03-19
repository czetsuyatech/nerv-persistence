package com.czetsuyatech.nerv.persistence.entity;

import java.io.Serializable;

/**
 * Generic contract for objects that expose a mutable identifier.
 *
 * @param <T> the identifier type
 */
public interface Id<T extends Serializable> {

  /**
   * Returns the identifier of this object.
   *
   * @return the current identifier, or {@code null} if not assigned yet
   */
  T getId();

  /**
   * Sets the identifier of this object.
   *
   * @param id the identifier to assign
   */
  void setId(T id);
}
