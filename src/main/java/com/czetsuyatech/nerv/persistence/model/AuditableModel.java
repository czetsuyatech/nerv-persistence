package com.czetsuyatech.nerv.persistence.model;

import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public abstract class AuditableModel extends BaseModel {

  private Instant created;
  private Instant updated;
  private String createdBy;
  private String updatedBy;
}
