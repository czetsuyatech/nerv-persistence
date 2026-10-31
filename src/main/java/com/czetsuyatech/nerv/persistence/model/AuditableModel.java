package com.czetsuyatech.nerv.persistence.model;

import java.io.Serializable;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Identifier-typed DTO with auditing metadata.
 *
 * @param <ID> identifier type
 */
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString(callSuper = true)
public abstract class AuditableModel<ID extends Serializable> extends BaseModel<ID> {

  private Instant created;
  private Instant updated;
  private String createdBy;
  private String updatedBy;
}
