package com.czetsuyatech.nerv.persistence.model;

import com.czetsuyatech.nerv.persistence.entity.Id;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public abstract class BaseModel<ID extends Serializable> implements Id<ID> {

  private ID id;
}
