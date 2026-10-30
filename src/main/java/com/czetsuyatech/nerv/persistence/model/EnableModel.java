package com.czetsuyatech.nerv.persistence.model;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Identifier-typed DTO supporting both numeric and UUID identifiers.
 *
 * @param <ID> identifier type
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class EnableModel<ID extends Serializable> extends AuditableModel<ID> {

  private boolean activeStatus = false;

  public boolean isEnabled() {
    return activeStatus;
  }
}
