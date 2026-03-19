package com.czetsuyatech.nerv.persistence.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

/**
 * Extension contract for executing {@link Specification}-based queries with projections.
 *
 * @param <T> root entity type
 */
public interface JpaSpecificationExecutorWithProjection<T> {

  /**
   * Returns a single projected result matching the given specification.
   *
   * @param spec specification used to filter results
   * @param projectionType target projection type
   * @param <S> projection type
   * @return the projected result, or {@code null} if none found
   */
  <S> S findOne(Specification<T> spec, Class<S> projectionType);

  /**
   * Returns all projected results matching the given specification.
   *
   * @param spec specification used to filter results
   * @param projectionType target projection type
   * @param <S> projection type
   * @return matching projected results
   */
  <S> List<S> findAll(Specification<T> spec, Class<S> projectionType);

  /**
   * Returns a page of projected results matching the given specification.
   *
   * @param spec specification used to filter results
   * @param pageable paging information
   * @param projectionType target projection type
   * @param <S> projection type
   * @return a page of matching projected results
   */
  <S> Page<S> findAll(Specification<T> spec, Pageable pageable, Class<S> projectionType);

  /**
   * Returns all projected results matching the given specification and sort order.
   *
   * @param spec specification used to filter results
   * @param sort sort information
   * @param projectionType target projection type
   * @param <S> projection type
   * @return matching projected results
   */
  <S> List<S> findAll(Specification<T> spec, Sort sort, Class<S> projectionType);

  /**
   * Returns a page of projected results without an explicit specification.
   *
   * @param pageable paging information
   * @param projectionType target projection type
   * @param <S> projection type
   * @return a page of projected results
   */
  <S> Page<S> findAll(Pageable pageable, Class<S> projectionType);

  /**
   * Returns a slice of projected results matching the given specification.
   *
   * @param spec specification used to filter results
   * @param pageable paging information
   * @param projectionType target projection type
   * @param <S> projection type
   * @return a slice of matching projected results
   */
  <S> Slice<S> findAllSlice(Specification<T> spec, Pageable pageable, Class<S> projectionType);

  /**
   * Returns the number of instances matching the given specification.
   *
   * @param spec specification used to count results
   * @return number of matching instances
   */
  long count(Specification<T> spec);
}
