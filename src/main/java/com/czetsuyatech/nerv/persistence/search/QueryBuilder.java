package com.czetsuyatech.nerv.persistence.search;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.springframework.data.jpa.domain.Specification;

/**
 * Utility class for building {@link Specification} instances from a compact search string.
 *
 * <p>The builder parses a query expression into {@link SearchCriteria} items,
 * creates specification instances using a constructor that accepts a single
 * {@link SearchCriteria}, and combines them using logical {@code and} operations.
 *
 * <p>An optional enum may be supplied to whitelist searchable field names.
 * When provided, only criteria whose keys match an enum constant are included.
 */
public class QueryBuilder {

  /**
   * Pattern used to parse search expressions in the form {@code field<op>value,}.
   */
  private static final Pattern PATTERN = Pattern.compile("(\\w+?)(<=|>=|[:<>/*~])(.+?),");

  /**
   * Utility class constructor.
   */
  private QueryBuilder() {

  }

  /**
   * Builds a {@link Specification} from the provided search string.
   *
   * @param search the raw search expression
   * @param specification the specification implementation class with a constructor accepting {@link SearchCriteria}
   * @param e optional enum class used to validate allowed field names
   * @param <T> the entity type targeted by the specification
   * @param <V> the specification implementation type
   * @return a composed specification, or {@link Specification#unrestricted()} when the input is blank or contains no valid criteria
   * @throws NoSuchMethodException if the specification class does not expose the expected constructor
   * @throws InstantiationException if the specification cannot be instantiated
   * @throws IllegalAccessException if the constructor is not accessible
   * @throws InvocationTargetException if constructor invocation fails
   */
  public static <T, V> Specification<T> build(String search, Class<V> specification, Class<? extends Enum<?>> e)
      throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

    if (search == null || search.isBlank()) {
      return Specification.unrestricted();
    }

    List<SearchCriteria> params = new ArrayList<>();
    Matcher matcher = PATTERN.matcher(search + ",");

    while (matcher.find()) {
      getListParams(matcher.group(1), matcher.group(2), matcher.group(3), params, e);
    }

    if (params.isEmpty()) {
      return Specification.unrestricted();
    }

    Constructor<V> constructor = specification.getConstructor(SearchCriteria.class);
    Specification<T> result = Specification.unrestricted();

    for (SearchCriteria param : params) {
      result = result.and((Specification<T>) constructor.newInstance(param));
    }

    return result;
  }

  /**
   * Adds a parsed search criterion to the target list when it is valid and allowed.
   *
   * @param key the field name
   * @param operation the operator symbol
   * @param value the comparison value
   * @param params the target list of parsed criteria
   * @param e optional enum used to validate allowed field names
   */
  private static void getListParams(String key, String operation, Object value, List<SearchCriteria> params,
      Class<? extends Enum<?>> e) {

    if (key == null || key.isBlank() || operation == null || operation.isBlank() || value == null) {
      return;
    }

    if (e == null) {
      params.add(new SearchCriteria(key, operation, value));
      return;
    }

    List<String> availableFields = Stream.of(e.getEnumConstants())
        .map(Enum::toString)
        .toList();

    if (availableFields.contains(key)) {
      params.add(new SearchCriteria(key, operation, value));
    }
  }
}
