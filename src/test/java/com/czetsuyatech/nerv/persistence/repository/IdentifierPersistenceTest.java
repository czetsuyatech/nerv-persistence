package com.czetsuyatech.nerv.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.czetsuyatech.nerv.persistence.config.NervDataJpaTest;
import com.czetsuyatech.nerv.persistence.entity.UserEntity;
import com.czetsuyatech.nerv.persistence.entity.UuidProductEntity;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {UserRepository.class, UuidProductRepository.class})
@ActiveProfiles("tst")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@NervDataJpaTest
class IdentifierPersistenceTest {

  @Autowired private UserRepository users;
  @Autowired private UuidProductRepository products;
  @Autowired private EntityManager entityManager;

  @Test
  void persistsLongAndUuidEntitiesInTheSamePersistenceUnit() {
    var user = new UserEntity();
    user.setFirstName("Long identifier");
    var product = UuidProductEntity.builder().code("UUID").name("Product")
        .activeStatus(true).tags(List.of("uuid-foreign-key")).build();
    assertThat(user.isTransient()).isTrue();
    assertThat(product.isTransient()).isTrue();

    users.saveAndFlush(user);
    products.saveAndFlush(product);
    Long userId = user.getId();
    UUID productId = product.getId();
    assertThat(userId).isNotNull();
    assertThat(productId).isNotNull();
    assertThat(user.isTransient()).isFalse();
    assertThat(product.isTransient()).isFalse();
    assertThat(product.getVersion()).isZero();
    assertThat(product.getCreated()).isNotNull();
    assertThat(product.getUpdated()).isNotNull();
    entityManager.clear();

    assertThat(users.findById(userId)).isPresent();
    var loaded = products.findById(productId).orElseThrow();
    assertThat(loaded.getCode()).isEqualTo("UUID");
    assertThat(loaded.isActiveStatus()).isTrue();
    assertThat(loaded.getTags()).containsExactly("uuid-foreign-key");
    loaded.setName("Updated");
    products.flush();
    assertThat(loaded.getVersion()).isEqualTo(1);
    entityManager.clear();
    assertThat(products.findById(productId).orElseThrow().getName()).isEqualTo("Updated");
    products.deleteById(productId);
    products.flush();
    entityManager.clear();
    assertThat(products.findById(productId)).isEmpty();
  }

  @Test
  void supportsTypedUuidSpecificationsSlicesAndProjections() {
    var product = products.saveAndFlush(UuidProductEntity.builder().code("SEARCH").build());
    Specification<UuidProductEntity> byId = (root, query, builder) ->
        builder.equal(root.get("id"), product.getId());

    assertThat(products.findAllSlice(byId, Pageable.ofSize(10)).getContent())
        .extracting(UuidProductEntity::getId).containsExactly(product.getId());
    var projection = products.findOne(byId, ProductProjection.class);
    assertThat(projection.getId()).isEqualTo(product.getId());
    assertThat(projection.getCode()).isEqualTo("SEARCH");
  }

  @Test
  void rejectsUpdatesWithAStaleUuidEntityVersion() {
    var stale = products.saveAndFlush(UuidProductEntity.builder().code("LOCK").build());
    entityManager.clear();
    var current = products.findById(stale.getId()).orElseThrow();
    current.setName("New version");
    products.flush();
    entityManager.clear();
    stale.setName("Stale version");

    assertThatThrownBy(() -> products.saveAndFlush(stale))
        .isInstanceOf(ObjectOptimisticLockingFailureException.class);
  }

  public interface ProductProjection {
    UUID getId();
    String getCode();
  }
}
