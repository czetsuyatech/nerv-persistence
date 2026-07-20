package com.czetsuyatech.nerv.persistence.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResultListModel<T> {
    private List<T> result;
}
