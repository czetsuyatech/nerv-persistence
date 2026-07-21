package com.czetsuyatech.nerv.persistence.model;

import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString(callSuper = true)
public abstract class AuditableModel extends BaseModel {

  private Instant created;
  private Instant updated;
  private String createdBy;
  private String updatedBy;
}
