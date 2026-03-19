package com.czetsuyatech.nerv.persistence.search;

import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.core.PropertyPath;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

/**
 * Utility methods for building and materializing projection queries.
 */
public class QueryProjectionUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(QueryProjectionUtils.class);
  private static final ProjectionFactory PROJECTION_FACTORY = new SpelAwareProxyProjectionFactory();

  /**
   * Utility class constructor.
   */
  private QueryProjectionUtils() {
  }

  /**
   * Creates a set of projected selections for the supplied projection type.
   *
   * @param root query root
   * @param rootType root entity type
   * @param projectionType projection interface or DTO type
   * @param <T> root entity type
   * @param <R> projection type
   * @return the set of JPA selections used to build a tuple query
   */
  public static <T, R> Set<Selection<?>> createProjectedSelection(Root<T> root, Class<T> rootType,
      Class<R> projectionType) {

    Set<Selection<?>> selections = new HashSet<>();
    List<PropertyDescriptor> inputProperties = PROJECTION_FACTORY.getProjectionInformation(projectionType)
        .getInputProperties();

    inputProperties.forEach(propertyDescriptor -> {
      String property = propertyDescriptor.getName();
      PropertyPath path = PropertyPath.from(property, rootType);
      selections.add(toExpressionRecursively(root, path).alias(property));
    });

    return selections;
  }

  /**
   * Creates a projection instance from a tuple result.
   *
   * @param tuple tuple result
   * @param projectionType projection type
   * @param <T> projection type
   * @return the created projection
   */
  @SuppressWarnings("unchecked")
  public static <T> T createProjection(Tuple tuple, Class<T> projectionType) {
    Map<String, Object> mappedResult = createMappedResult(tuple);
    return (T) PROJECTION_FACTORY.createProjection(projectionType, mappedResult);
  }

  /**
   * Creates projection instances from tuple results.
   *
   * @param tuples tuple results
   * @param projectionType projection type
   * @param <T> projection type
   * @return the created projections
   */
  public static <T> List<T> createProjection(List<Tuple> tuples, Class<T> projectionType) {
    return tuples.stream()
        .map(tuple -> createProjection(tuple, projectionType))
        .toList();
  }

  /**
   * Resolves an expression recursively for nested property paths.
   *
   * @param from source path
   * @param property property path
   * @param <T> expression type
   * @return the resolved expression, or {@code null} if reflection access fails
   */
  @SuppressWarnings("unchecked")
  public static <T> Expression<T> toExpressionRecursively(From<?, ?> from, PropertyPath property) {

    Class<QueryUtils> queryUtilsClass = QueryUtils.class;

    try {
      Method method = queryUtilsClass.getDeclaredMethod("toExpressionRecursively", From.class, PropertyPath.class);
      method.setAccessible(true);
      return (Expression<T>) method.invoke(queryUtilsClass, from, property);

    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      LOGGER.error("Error on generating expression recursively.", e);

      return null;
    }
  }

  /**
   * Maps tuple aliases to their values.
   *
   * @param tuple tuple result
   * @return alias-to-value map
   */
  private static Map<String, Object> createMappedResult(Tuple tuple) {

    Map<String, Object> mappedResult = new HashMap<>(tuple.getElements().size());
    tuple.getElements().forEach(tupleElement -> {
      String name = tupleElement.getAlias();
      mappedResult.put(name, tuple.get(name));
    });

    return mappedResult;
  }
}
