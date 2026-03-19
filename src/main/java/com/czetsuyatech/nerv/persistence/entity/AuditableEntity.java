package com.czetsuyatech.nerv.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Base entity that adds auditing metadata.
 *
 * <p>Stores creation and last modification timestamps together with the
 * associated user information. Auditing values are populated through
 * Spring Data JPA auditing support.
 */
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity extends BaseEntity {

  /**
   * Timestamp when the entity was created.
   */
  @CreatedDate
  @Column(name = "created", nullable = false, updatable = false)
  private Instant created;

  /**
   * Timestamp when the entity was last updated.
   */
  @LastModifiedDate
  @Column(name = "updated")
  private Instant updated;

  /**
   * Identifier of the user who created the entity.
   */
  @CreatedBy
  @Column(name = "created_by")
  private String createdBy;

  /**
   * Identifier of the user who last updated the entity.
   */
  @LastModifiedBy
  @Column(name = "updated_by")
  private String updatedBy;
}
