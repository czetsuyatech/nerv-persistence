package com.czetsuyatech.nerv.persistence.model;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = false)
public abstract class BusinessModel<ID extends Serializable> extends EnableModel implements RefDataModel<ID> {

  private String code;
  private String name;

  @Override
  public boolean isEnabled() {
    return super.isEnabled();
  }

  @Override
  public Integer getSortOrder() {
    return 0;
  }
}
