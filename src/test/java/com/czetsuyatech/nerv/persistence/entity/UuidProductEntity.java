package com.czetsuyatech.nerv.persistence.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/** Test fixture exercising every UUID mapped superclass. */
@Entity
@Table(name = "uuid_product")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class UuidProductEntity extends UuidBusinessEntity {

  @ElementCollection
  @CollectionTable(name = "uuid_product_tag", joinColumns = @JoinColumn(name = "product_id"))
  @Column(name = "tag")
  private List<String> tags;
}
