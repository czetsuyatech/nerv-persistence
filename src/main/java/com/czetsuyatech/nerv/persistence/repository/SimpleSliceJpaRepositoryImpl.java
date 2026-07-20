package com.czetsuyatech.nerv.persistence.repository;

import com.czetsuyatech.nerv.persistence.specification.QueryProjectionUtils;
import com.czetsuyatech.nerv.persistence.specification.SimpleSliceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.Assert;

/**
 * Custom repository base implementation that adds slice queries and projection support.
 *
 * @param <T> entity type
 * @param <I> identifier type
 */
public class SimpleSliceJpaRepositoryImpl<T, I extends Serializable> extends SimpleJpaRepository<T, I> implements
    SliceJpaRepository<T, I> {

  private static final Logger LOGGER = LoggerFactory.getLogger(SimpleSliceJpaRepositoryImpl.class);

  /**
   * Backing entity manager used for criteria and projection queries.
   */
  private final EntityManager entityManager;

  /**
   * Creates a repository instance from JPA entity metadata.
   *
   * @param entityInformation entity information metadata
   * @param entityManager entity manager
   */
  public SimpleSliceJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
    super(entityInformation, entityManager);
    Assert.notNull(entityManager, "EntityManager must not be null!");
    this.entityManager = entityManager;
  }

  /**
   * Creates a repository instance for the given domain class.
   *
   * @param domainClass domain class
   * @param entityManager entity manager
   */
  public SimpleSliceJpaRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
    super(domainClass, entityManager);
    this.entityManager = entityManager;
  }

  /**
   * Reads a page while fetching one extra record to determine whether a next page exists.
   *
   * @param query source query
   * @param domainClass domain type
   * @param pageable paging information
   * @param spec applied specification
   * @param <S> result type
   * @return a page of results
   */
  protected <S extends T> Page<S> readPage(TypedQuery<S> query, Class<S> domainClass, Pageable pageable,
      Specification<S> spec) {

    if (pageable.isPaged()) {
      query.setFirstResult((int) pageable.getOffset());
      int pageSize = pageable.getPageSize();
      if (pageable.getPageSize() < Integer.MAX_VALUE) {
        pageSize++;
      }
      query.setMaxResults(pageSize);
    }

    List<S> content = query.getResultList();
    int totalKnownElements = (int) pageable.getOffset() + content.size();
    if (content.size() > pageable.getPageSize()) {
      content.remove(content.size() - 1);
    }

    return PageableExecutionUtils.getPage(content, pageable, () -> (long) totalKnownElements);
  }

  /**
   * Returns a slice of entities matching the supplied specification.
   *
   * @param spec filter specification
   * @param pageable paging information
   * @return a slice of entities
   */
  @Override
  public Slice<T> findAllSlice(Specification<T> spec, Pageable pageable) {
    Page<T> result = this.findAll(spec, pageable);
    return new SimpleSliceImpl<>(result.getContent(), pageable, result.hasNext());
  }

  /**
   * Returns a single projected result matching the supplied specification.
   *
   * @param spec filter specification
   * @param projectionType projection type
   * @param <S> projection type
   * @return matching projection or {@code null} if none exists
   */
  @Override
  @SuppressWarnings("unchecked")
  public <S> S findOne(Specification<T> spec, Class<S> projectionType) {

    CriteriaQuery<Tuple> criteriaQuery = this.createQuery(spec, (Sort) null, projectionType);
    TypedQuery<Tuple> query = this.entityManager.createQuery(criteriaQuery);
    query.setMaxResults(1);

    try {
      Tuple tuple = query.getSingleResult();
      return (S) QueryProjectionUtils.createProjection(tuple, projectionType);
    } catch (NoResultException e) {
      LOGGER.warn(e.getMessage(), e);
      return null;
    }
  }

  /**
   * Returns all projected results matching the supplied specification.
   *
   * @param spec filter specification
   * @param projectionType projection type
   * @param <S> projection type
   * @return matching projected results
   */
  @Override
  public <S> List<S> findAll(Specification<T> spec, Class<S> projectionType) {

    CriteriaQuery<Tuple> criteriaQuery = this.createQuery(spec, (Sort) null, projectionType);
    TypedQuery<Tuple> query = this.entityManager.createQuery(criteriaQuery);
    List<Tuple> results = query.getResultList();

    return QueryProjectionUtils.createProjection(results, projectionType);
  }

  /**
   * Returns a page of projected results matching the supplied specification.
   *
   * @param spec filter specification
   * @param pageable paging information
   * @param projectionType projection type
   * @param <S> projection type
   * @return a page of projected results
   */
  @Override
  public <S> Page<S> findAll(Specification<T> spec, Pageable pageable, Class<S> projectionType) {

    CriteriaQuery<Tuple> criteriaQuery = this.createQuery(spec, pageable.getSort(), projectionType);
    TypedQuery<Tuple> query = this.entityManager.createQuery(criteriaQuery);
    query.setFirstResult((int) pageable.getOffset());
    query.setMaxResults(pageable.getPageSize());
    List<Tuple> results = query.getResultList();
    List<S> projectedResults = QueryProjectionUtils.createProjection(results, projectionType);

    return this.readPage(projectedResults, this.getEntityType(), pageable, spec);
  }

  /**
   * Returns all projected results matching the supplied specification and sort order.
   *
   * @param spec filter specification
   * @param sort sort information
   * @param projectionType projection type
   * @param <S> projection type
   * @return matching projected results
   */
  @Override
  public <S> List<S> findAll(Specification<T> spec, Sort sort, Class<S> projectionType) {

    CriteriaQuery<Tuple> criteriaQuery = this.createQuery(spec, sort, projectionType);
    TypedQuery<Tuple> query = this.entityManager.createQuery(criteriaQuery);
    List<Tuple> results = query.getResultList();

    return QueryProjectionUtils.createProjection(results, projectionType);
  }

  /**
   * Returns a page of projected results without an explicit specification.
   *
   * @param pageable paging information
   * @param projectionType projection type
   * @param <S> projection type
   * @return a page of projected results
   */
  @Override
  @SuppressWarnings("unchecked")
  public <S> Page<S> findAll(Pageable pageable, Class<S> projectionType) {
    return this.findAll((Specification<T>) null, pageable, projectionType);
  }

  /**
   * Returns a slice of projected results matching the supplied specification.
   *
   * @param spec filter specification
   * @param pageable paging information
   * @param projectionType projection type
   * @param <S> projection type
   * @return a slice of projected results
   */
  @Override
  public <S> Slice<S> findAllSlice(Specification<T> spec, Pageable pageable, Class<S> projectionType) {

    Page<S> result = this.findAll(spec, pageable, projectionType);
    return new SimpleSliceImpl<>(result.getContent(), pageable, result.hasNext());
  }

  /**
   * Creates a tuple query configured for the requested projection.
   *
   * @param spec filter specification
   * @param sort sort information
   * @param projectionType projection type
   * @param <S> projection type
   * @return configured criteria query
   */
  private <S> CriteriaQuery<Tuple> createQuery(Specification<T> spec, Sort sort, Class<S> projectionType) {

    CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
    CriteriaQuery<Tuple> query = criteriaBuilder.createTupleQuery();
    Root<T> root = this.applySpecificationToCriteria(spec, this.getEntityType(), query);
    Set<Selection<?>> selections = QueryProjectionUtils.createProjectedSelection(root, this.getEntityType(),
        projectionType);
    query.multiselect(new ArrayList<>(selections));
    if (sort != null) {
      query.orderBy(QueryUtils.toOrders(sort, root, criteriaBuilder));
    }

    return query;
  }

  /**
   * Applies the given specification to the criteria query.
   *
   * @param spec filter specification
   * @param entityClass entity type
   * @param query criteria query
   * @param <S> query result type
   * @return query root
   */
  private <S> Root<T> applySpecificationToCriteria(Specification<T> spec, Class<T> entityClass,
      CriteriaQuery<S> query) {

    Root<T> root = query.from(entityClass);
    if (spec != null) {
      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      Predicate predicate = spec.toPredicate(root, query, builder);
      if (predicate != null) {
        query.where(predicate);
      }
    }

    return root;
  }

  /**
   * Executes a count query and aggregates non-null count fragments.
   *
   * @param query count query
   * @return total count
   */
  private static Long executeCountQuery(TypedQuery<Long> query) {
    List<Long> totals = query.getResultList();
    long total = 0L;

    for (Long element : totals) {
      total += element == null ? 0L : element;
    }

    return total;
  }

  /**
   * Builds a page from already projected results.
   *
   * @param resultList projected results
   * @param domainClass domain class
   * @param pageable paging information
   * @param spec filter specification
   * @param <S> result type
   * @return a page of projected results
   */
  protected <S> Page<S> readPage(List<S> resultList, Class<T> domainClass, Pageable pageable, Specification<T> spec) {
    return PageableExecutionUtils.getPage(resultList, pageable,
        () -> executeCountQuery(this.getCountQuery(spec, domainClass)));
  }

  /**
   * Returns the managed entity type of this repository.
   *
   * @return the entity type
   */
  private Class<T> getEntityType() {
    return this.getDomainClass();
  }
}
