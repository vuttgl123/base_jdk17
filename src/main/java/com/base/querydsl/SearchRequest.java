package com.base.querydsl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchRequest {

    @Builder.Default
    private List<FilterRequest> filters = new ArrayList<>();

    @Builder.Default
    private String logic = "AND";

    @Builder.Default
    private Integer page = 0;

    @Builder.Default
    private Integer size = 20;

    private String sortBy;

    @Builder.Default
    private String sortDirection = "DESC";

    @Builder.Default
    private Boolean includeDeleted = false;
}
