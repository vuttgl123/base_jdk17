package com.base.querydsl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterRequest {
    private String field;
    private String operator; // eq, ne, gt, gte, lt, lte, like, in, between
    private Object value;
    private Object secondValue;

    public FilterRequest(String field, String operator, Object value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }
}
