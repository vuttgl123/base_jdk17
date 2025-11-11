package com.base.service.inter;

import com.base.model.entity.BaseEntity3;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public interface BaseService<T extends BaseEntity3, RQ, RS> {
    RS create(RQ request);
    Optional<RS> getById(Long id);
    Page<RS> getPage(Pageable pageable, Specification<T> spec);
    RS update(Long id, RQ request);
    void softDelete(Long id);
    void restore(Long id);
}
