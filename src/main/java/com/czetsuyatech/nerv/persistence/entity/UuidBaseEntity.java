package com.czetsuyatech.nerv.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Base mapped superclass for persistent entities.
 *
 * <p>Provides a generated UUID identifier, optimistic locking support,
 * and common utility constants for numeric precision and scale.
 */
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public abstract class UuidBaseEntity implements Serializable, IEntity, Id<UUID> {

  /**
   * Default precision for numeric database columns.
   */
  public static final int NB_PRECISION = 23;

  /**
   * Default scale for numeric database columns.
   */
  public static final int NB_SCALE = 12;

  @jakarta.persistence.Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id")
  private UUID id;

  @Version
  @Column(name = "version")
  private Integer version;

  /**
   * Indicates whether this entity has an unassigned identifier.
   *
   * <p>Identifiers are assigned by the persistence provider. This is not a persistence-state
   * check for instances whose identifier has been assigned manually.
   *
   * @return {@code true} if the identifier is unset; {@code false} otherwise
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
    return "UuidBaseEntity [entityId=" + id + "]";
  }
}
