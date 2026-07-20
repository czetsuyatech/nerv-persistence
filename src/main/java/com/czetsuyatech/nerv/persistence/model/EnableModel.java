package com.czetsuyatech.nerv.persistence.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class EnableModel extends AuditableModel {

  private boolean activeStatus = false;

  public boolean isEnabled() {
    return activeStatus;
  }
}
