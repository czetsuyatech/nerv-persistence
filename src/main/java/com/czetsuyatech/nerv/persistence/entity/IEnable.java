package com.czetsuyatech.nerv.persistence.entity;

/**
 * Contract for objects that expose an enabled or active state.
 */
public interface IEnable {

  /**
   * Indicates whether this object is enabled.
   *
   * @return {@code true} if enabled; {@code false} otherwise
   */
  boolean isActiveStatus();
}
