package com.czetsuyatech.nerv.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Base auditable entity that adds an activation flag.
 *
 * <p>Intended for entities that can be enabled or disabled without
 * being physically removed from persistence.
 */
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString(callSuper = true)
public abstract class EnableEntity extends AuditableEntity {

  /**
   * Indicates whether the entity is active.
   */
  @Column(name = "active_status")
  private boolean activeStatus;
}
