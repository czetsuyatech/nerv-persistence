package com.czetsuyatech.nerv.persistence.model;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@NoArgsConstructor
public class SliceModel<T> {

  private int number;
  private int size;
  private int numberOfElements;
  private List<T> content;
  private boolean hasContent;
  private boolean first;
  private boolean last;
  private boolean hasNext;
  private boolean hasPrevious;
  private Pageable pageable;

  public boolean hasContent() {
    return this.hasContent;
  }

  public boolean hasNext() {
    return this.hasNext;
  }

  public boolean hasPrevious() {
    return this.hasPrevious;
  }
}
