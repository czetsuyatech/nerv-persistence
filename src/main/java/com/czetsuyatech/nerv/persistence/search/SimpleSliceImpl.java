package com.czetsuyatech.nerv.persistence.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.function.Function;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

/**
 * Simple {@link Slice} implementation used for JSON-friendly slice results.
 *
 * <p>The pageable and sort metadata are ignored during Jackson serialization
 * to keep the payload compact.
 *
 * @param <T> the content element type
 */
@JsonIgnoreProperties({"pageable", "sort"})
public class SimpleSliceImpl<T> extends SliceImpl<T> {

  /**
   * Creates a new slice with explicit paging metadata.
   *
   * @param content the slice content
   * @param pageable the paging information
   * @param hasNext whether another slice exists after the current one
   */
  public SimpleSliceImpl(List<T> content, Pageable pageable, boolean hasNext) {
    super(content, pageable, hasNext);
  }

  /**
   * Creates a new slice with content only.
   *
   * @param content the slice content
   */
  public SimpleSliceImpl(List<T> content) {
    super(content);
  }

  /**
   * Maps the content of this slice into another type.
   *
   * @param converter the mapping function
   * @param <U> the target element type
   * @return a mapped slice preserving paging state
   */
  @Override
  public <U> Slice<U> map(Function<? super T, ? extends U> converter) {
    return new SliceImpl<>(this.getConvertedContent(converter), getPageable(), hasNext());
  }
}
