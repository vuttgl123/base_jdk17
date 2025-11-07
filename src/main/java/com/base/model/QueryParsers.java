package com.base.model;

import com.base.model.BaseSpecification;
import com.base.model.Filter;
import com.base.model.entity.BaseEntity;
import com.base.model.enumeration.FilterOp;
import com.base.model.enumeration.Scope;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public final class QueryParsers {
    private QueryParsers() {}

    public static Pageable resolvePageable(HttpServletRequest req) {
        int page = parseInt(req.getParameter("page"), 0);
        int size = parseInt(req.getParameter("size"), 20);
        List<String> sorts = getParams(req, "sort");
        if (sorts.isEmpty()) return PageRequest.of(page, size);
        List<Sort.Order> orders = new ArrayList<>();
        for (String s : sorts) {
            String[] arr = s.split(",");
            String field = arr[0].trim();
            Sort.Direction dir = (arr.length > 1 && "desc".equalsIgnoreCase(arr[1])) ? Sort.Direction.DESC : Sort.Direction.ASC;
            orders.add(new Sort.Order(dir, field));
        }
        return PageRequest.of(page, size, Sort.by(orders));
    }

    public static Scope resolveScope(HttpServletRequest req) {
        String raw = Optional.ofNullable(req.getParameter("scope")).orElse("active").toUpperCase();
        try { return Scope.valueOf(raw); } catch (IllegalArgumentException e) { return Scope.ACTIVE; }
    }

    public static List<Filter> parseFilters(HttpServletRequest req) {
        Map<String, String[]> map = req.getParameterMap();
        List<Filter> result = new ArrayList<>();
        for (String key : map.keySet()) {
            if (!key.startsWith("f.")) continue; // filter prefix
            String field = key.substring(2);
            for (String raw : map.get(key)) {
                int idx = raw.indexOf(":");
                String opStr = idx > -1 ? raw.substring(0, idx) : "eq";
                String valuePart = idx > -1 ? raw.substring(idx + 1) : raw;
                FilterOp op = FilterOp.valueOf(opStr.toLowerCase());
                List<String> values = Arrays.stream(valuePart.split(",")).map(String::trim).filter(v -> !v.isEmpty()).collect(Collectors.toList());
                result.add(new Filter(field, op, values));
            }
        }
        return result;
    }

    public static <T extends BaseEntity> Specification<T> buildSpecification(String keyword, List<String> keywordFields, List<Filter> filters, Scope scope) {
        Specification<T> spec = BaseSpecification.scope(scope);
        if (!BaseSpecification.isBlank(keyword) && keywordFields != null && !keywordFields.isEmpty()) {
            Specification<T> orSpec = null;
            for (String f : keywordFields) {
                Specification<T> s = BaseSpecification.like(f, keyword);
                orSpec = (orSpec == null) ? s : orSpec.or(s);
            }
            if (orSpec != null) spec = spec.and(orSpec);
        }
        if (filters != null) {
            for (Filter f : filters) {
                spec = spec.and(applyFilter(f));
            }
        }
        return spec;
    }

    private static <T> Specification<T> applyFilter(Filter f) {
        return (root, query, cb) -> {
            String field = f.getField();
            List<String> vals = f.getValues();
            return switch (f.getOp()) {
                case eq -> cb.equal(root.get(field), cast(root.get(field).getJavaType(), vals.get(0)));
                case ne -> cb.notEqual(root.get(field), cast(root.get(field).getJavaType(), vals.get(0)));
                case like -> cb.like(cb.lower(root.get(field)), "%" + vals.get(0).toLowerCase() + "%");
                case in -> root.get(field).in(vals.stream().map(v -> cast(root.get(field).getJavaType(), v)).toList());
                case gt -> cb.greaterThan(root.get(field), (Comparable) cast(root.get(field).getJavaType(), vals.get(0)));
                case gte -> cb.greaterThanOrEqualTo(root.get(field), (Comparable) cast(root.get(field).getJavaType(), vals.get(0)));
                case lt -> cb.lessThan(root.get(field), (Comparable) cast(root.get(field).getJavaType(), vals.get(0)));
                case lte -> cb.lessThanOrEqualTo(root.get(field), (Comparable) cast(root.get(field).getJavaType(), vals.get(0)));
                case between -> {
                    Comparable v1 = (Comparable) cast(root.get(field).getJavaType(), vals.size() > 0 ? vals.get(0) : null);
                    Comparable v2 = (Comparable) cast(root.get(field).getJavaType(), vals.size() > 1 ? vals.get(1) : null);
                    if (v1 != null && v2 != null) yield cb.between(root.get(field), v1, v2);
                    if (v1 != null) yield cb.greaterThanOrEqualTo(root.get(field), v1);
                    if (v2 != null) yield cb.lessThanOrEqualTo(root.get(field), v2);
                    yield null;
                }
                case isnull -> cb.isNull(root.get(field));
                case isnotnull -> cb.isNotNull(root.get(field));
            };
        };
    }

    private static Object cast(Class<?> type, String value) {
        if (value == null) return null;
        if (type.equals(String.class)) return value;
        if (type.equals(Long.class) || type.equals(long.class)) return Long.valueOf(value);
        if (type.equals(Integer.class) || type.equals(int.class)) return Integer.valueOf(value);
        if (type.equals(Double.class) || type.equals(double.class)) return Double.valueOf(value);
        if (type.equals(Float.class) || type.equals(float.class)) return Float.valueOf(value);
        if (type.equals(Boolean.class) || type.equals(boolean.class)) return Boolean.valueOf(value);
        if (type.equals(BigDecimal.class)) return new BigDecimal(value);
        if (type.equals(Instant.class)) return Instant.parse(value);
        if (type.equals(LocalDate.class)) return LocalDate.parse(value);
        if (type.equals(LocalDateTime.class)) return LocalDateTime.parse(value);
        return value;
    }

    private static int parseInt(String s, int def) {
        try { return (s == null) ? def : Integer.parseInt(s); } catch (NumberFormatException e) { return def; }
    }

    private static List<String> getParams(HttpServletRequest req, String name) {
        String[] arr = req.getParameterValues(name);
        if (arr == null) return List.of();
        return Arrays.stream(arr).filter(v -> v != null && !v.isBlank()).toList();
    }

}