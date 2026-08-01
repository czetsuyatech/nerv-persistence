package com.czetsuyatech.nerv.persistence.model;

import java.io.Serializable;

public interface RefDataModel<ID extends Serializable> {

  String getCode();

  String getName();

  boolean isEnabled();

  Integer getSortOrder();
}
