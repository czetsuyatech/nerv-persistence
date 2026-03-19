package com.czetsuyatech.nerv.persistence.entity;

import java.io.Serializable;

/**
 * Base contract for persistence entities.
 *
 * <p>Provides access to the entity identifier and a way to determine whether
 * the entity has already been persisted.
 */
public interface IEntity {

  /**
   * Returns the identifier of this entity.
   *
   * @return the entity identifier, or {@code null} if the entity is not yet persisted
   */
  Serializable getId();

  /**
   * Indicates whether this entity is transient.
   *
   * @return {@code true} if the entity has not been persisted yet; {@code false} otherwise
   */
  boolean isTransient();
}
