package com.base.model;

import com.base.model.entity.BaseEntity3;
import com.base.model.enumeration.Scope;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;

public final class BaseSpecification {
    private BaseSpecification() {}


    public static <T extends BaseEntity3> Specification<T> scope(Scope scope) {
        if (scope == null || scope == Scope.ACTIVE) {
            return (root, query, cb) -> cb.isFalse(root.get("deleted"));
        }
        if (scope == Scope.DELETED) {
            return (root, query, cb) -> cb.isTrue(root.get("deleted"));
        }
        return null;
    }


    public static <T> Specification<T> like(String field, String keyword) {
        if (isBlank(keyword)) return null;
        return (root, query, cb) -> cb.like(cb.lower(root.get(field)), "%" + keyword.toLowerCase() + "%");
    }


    public static <T> Specification<T> eq(String field, Object value) {
        if (value == null) return null;
        return (root, query, cb) -> cb.equal(root.get(field), value);
    }


    public static <T> Specification<T> ne(String field, Object value) {
        if (value == null) return null;
        return (root, query, cb) -> cb.notEqual(root.get(field), value);
    }


    public static <T, V> Specification<T> in(String field, Collection<V> values) {
        if (values == null || values.isEmpty()) return null;
        return (root, query, cb) -> root.get(field).in(values);
    }


    public static <T, N extends Comparable<N>> Specification<T> gt(String field, N value) {
        if (value == null) return null;
        return (root, query, cb) -> cb.greaterThan(root.get(field), value);
    }


    public static <T, N extends Comparable<N>> Specification<T> gte(String field, N value) {
        if (value == null) return null;
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(field), value);
    }


    public static <T, N extends Comparable<N>> Specification<T> lt(String field, N value) {
        if (value == null) return null;
        return (root, query, cb) -> cb.lessThan(root.get(field), value);
    }


    public static <T, N extends Comparable<N>> Specification<T> lte(String field, N value) {
        if (value == null) return null;
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(field), value);
    }


    public static <T, N extends Comparable<N>> Specification<T> between(String field, N from, N to) {
        if (from == null && to == null) return null;
        return (root, query, cb) -> {
            if (from != null && to != null) return cb.between(root.get(field), from, to);
            if (from != null) return cb.greaterThanOrEqualTo(root.get(field), from);
            return cb.lessThanOrEqualTo(root.get(field), to);
        };
    }


    public static boolean isBlank(String s) { return s == null || s.isBlank(); }
}
