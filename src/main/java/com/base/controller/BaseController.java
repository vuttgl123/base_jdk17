package com.base.controller;

import com.base.model.QueryParsers;
import com.base.model.entity.BaseEntity3;
import com.base.model.enumeration.Scope;
import com.base.service.inter.BaseService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class BaseController<T extends BaseEntity3, RQ, RS> {
    protected abstract BaseService<T, RQ, RS> service();
    protected List<String> keywordFields() { return List.of(); }
    protected Specification<T> extraSpec() { return null; }

    @PostMapping
    public ResponseEntity<RS> create(@RequestBody RQ request) {
        return ResponseEntity.ok(service().create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RS> getById(@PathVariable Long id) {
        return service().getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<RS>> page(HttpServletRequest req, @RequestParam(value = "q", required = false) String q) {
        Pageable pageable = QueryParsers.resolvePageable(req);
        var filters = QueryParsers.parseFilters(req);
        Scope scope = QueryParsers.resolveScope(req);
        Specification<T> spec = QueryParsers.buildSpecification(q, keywordFields(), filters, scope);
        Specification<T> extra = extraSpec();
        if (extra != null) spec = spec.and(extra);
        return ResponseEntity.ok(service().getPage(pageable, spec));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RS> update(@PathVariable Long id, @RequestBody RQ request) {
        return ResponseEntity.ok(service().update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service().softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        service().restore(id);
        return ResponseEntity.noContent().build();
    }
}
