package com.czetsuyatech.nerv.persistence.repository;

import java.io.Serializable;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Base repository contract adding slice-based queries and projection support.
 *
 * @param <ENTITY> entity type
 * @param <ID> identifier type
 */
@NoRepositoryBean
public interface SliceJpaRepository<ENTITY, ID extends Serializable> extends JpaRepository<ENTITY, ID>,
    JpaSpecificationExecutor<ENTITY>, JpaSpecificationExecutorWithProjection<ENTITY> {

  /**
   * Returns a slice of entities matching the given specification.
   *
   * @param specification filter specification, may be {@code null}
   * @param pageable paging information
   * @return a slice of matching entities
   */
  Slice<ENTITY> findAllSlice(@Nullable Specification<ENTITY> specification, Pageable pageable);
}
