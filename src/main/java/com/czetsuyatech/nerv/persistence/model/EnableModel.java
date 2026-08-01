package com.czetsuyatech.nerv.persistence.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class EnableModel extends AuditableModel {

  private boolean activeStatus = false;

  public boolean isEnabled() {
    return activeStatus;
  }
}
