package com.base.ddd.shared.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic search request with pagination and sorting
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {

    private String keyword;
    private List<String> searchFields;

    @Builder.Default
    private Integer page = 0;

    @Builder.Default
    private Integer size = 20;

    @Builder.Default
    private List<SortRequest> sorts = new ArrayList<>();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SortRequest {
        private String field;
        private Sort.Direction direction;
    }

    public Pageable toPageable() {
        if (sorts == null || sorts.isEmpty()) {
            return PageRequest.of(page, size);
        }

        List<Sort.Order> orders = new ArrayList<>();
        for (SortRequest sort : sorts) {
            orders.add(new Sort.Order(
                    sort.getDirection() != null ? sort.getDirection() : Sort.Direction.ASC,
                    sort.getField()
            ));
        }

        return PageRequest.of(page, size, Sort.by(orders));
    }

    public void addSort(String field, Sort.Direction direction) {
        if (sorts == null) {
            sorts = new ArrayList<>();
        }
        sorts.add(SortRequest.builder()
                .field(field)
                .direction(direction)
                .build());
    }

    public boolean hasKeyword() {
        return keyword != null && !keyword.trim().isEmpty();
    }

    public boolean hasSearchFields() {
        return searchFields != null && !searchFields.isEmpty();
    }

    public Integer getValidPage() {
        return page != null && page >= 0 ? page : 0;
    }

    public Integer getValidSize() {
        if (size == null || size <= 0) {
            return 20;
        }
        return Math.min(size, 100); // Max 100 items per page
    }
}