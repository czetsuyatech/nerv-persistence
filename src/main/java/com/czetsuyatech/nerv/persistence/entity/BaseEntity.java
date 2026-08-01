package com.czetsuyatech.nerv.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Base mapped superclass for persistent entities.
 *
 * <p>Provides a generated numeric identifier, optimistic locking support,
 * and common utility constants for numeric precision and scale.
 */
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public abstract class BaseEntity implements Serializable, IEntity {

  /**
   * Default precision for numeric database columns.
   */
  public static final int NB_PRECISION = 23;

  /**
   * Default scale for numeric database columns.
   */
  public static final int NB_SCALE = 12;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Version
  @Column(name = "version")
  private Integer version;

  /**
   * Indicates whether this entity is transient.
   *
   * @return {@code true} if the entity has not been persisted yet; {@code false} otherwise
   */
  @Override
  public boolean isTransient() {
    return id == null;
  }

  /**
   * Returns a simple string representation of this entity.
   *
   * @return a string containing the entity identifier
   */
  @Override
  public String toString() {
    return "BaseEntity [entityId=" + id + "]";
  }
}
