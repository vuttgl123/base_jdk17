package com.base.model;

import com.base.model.enumeration.FilterOp;
import lombok.Value;

import java.util.List;

@Value
public class Filter {
    String field;
    FilterOp op;
    List<String> values;
}
