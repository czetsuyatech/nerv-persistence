package com.czetsuyatech.nerv.persistence.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.UUID;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.junit.jupiter.api.Test;

class ModelTest {

  @Test
  void uuidBuilderIncludesIdentifierAndAllInheritedMetadata() {
    UUID id = UUID.randomUUID();
    Instant created = Instant.now();
    var model = UuidProductModel.builder().id(id).created(created).createdBy("creator")
        .updated(created).updatedBy("editor").activeStatus(true).code("CODE").name("Name").build();

    UUID typedId = model.getId();
    assertThat(typedId).isEqualTo(id);
    assertThat(model.getCreated()).isEqualTo(created);
    assertThat(model.getCreatedBy()).isEqualTo("creator");
    assertThat(model.getUpdatedBy()).isEqualTo("editor");
    assertThat(model.isEnabled()).isTrue();
    assertThat(model.getCode()).isEqualTo("CODE");
    assertThat(model.getName()).isEqualTo("Name");
    assertThat(model.getSortOrder()).isZero();
    UUID replacement = UUID.randomUUID();
    model.setId(replacement);
    assertThat(model.getId()).isEqualTo(replacement);
  }

  @Test
  void supportsLongDtoBuildersAndTypedMutableIdentifiers() {
    var typed = LongProductModel.builder().id(42L).code("LONG").build();
    Long id = typed.getId();
    assertThat(id).isEqualTo(42L);
    assertThat(typed.isEnabled()).isFalse();
    var enabled = new EnableModel<Long>();
    enabled.setId(7L);
    Long enabledId = enabled.getId();
    assertThat(enabledId).isEqualTo(7L);
  }

  @NoArgsConstructor
  @SuperBuilder
  static class UuidProductModel extends BusinessModel<UUID> {
  }

  @NoArgsConstructor
  @SuperBuilder
  static class LongProductModel extends BusinessModel<Long> {
  }
}
