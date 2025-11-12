package com.base.ddd.shared.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic filter request for dynamic queries
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterRequest {

    @Builder.Default
    private List<FilterCriteria> filters = new ArrayList<>();

    @Builder.Default
    private List<SortCriteria> sorts = new ArrayList<>();

    private Integer page;
    private Integer size;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FilterCriteria {
        private String field;
        private FilterOperator operator;
        private Object value;
        private List<Object> values;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SortCriteria {
        private String field;
        private SortDirection direction;
    }

    public enum FilterOperator {
        EQUALS,
        NOT_EQUALS,
        GREATER_THAN,
        GREATER_THAN_OR_EQUAL,
        LESS_THAN,
        LESS_THAN_OR_EQUAL,
        LIKE,
        NOT_LIKE,
        IN,
        NOT_IN,
        IS_NULL,
        IS_NOT_NULL,
        BETWEEN,
        CONTAINS,
        STARTS_WITH,
        ENDS_WITH
    }

    public enum SortDirection {
        ASC,
        DESC
    }

    public void addFilter(String field, FilterOperator operator, Object value) {
        if (filters == null) {
            filters = new ArrayList<>();
        }
        filters.add(FilterCriteria.builder()
                .field(field)
                .operator(operator)
                .value(value)
                .build());
    }

    public void addSort(String field, SortDirection direction) {
        if (sorts == null) {
            sorts = new ArrayList<>();
        }
        sorts.add(SortCriteria.builder()
                .field(field)
                .direction(direction)
                .build());
    }
}