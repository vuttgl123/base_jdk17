package com.base.model.request;

import com.base.model.Filter;
import com.base.model.enumeration.Scope;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@Value
@Builder
public class RequestSpec<T> {
    Pageable pageable;
    Scope scope;
    String keyword;
    @Singular
    List<Filter> filters;
    Specification<T> specification;
}
