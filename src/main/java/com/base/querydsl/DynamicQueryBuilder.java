package com.base.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Slf4j
public class DynamicQueryBuilder<E> {

    private final Class<E> entityClass;

    public DynamicQueryBuilder(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    public Predicate buildPredicate(SearchRequest searchRequest) {
        BooleanBuilder builder = new BooleanBuilder();
        PathBuilder<E> entityPath = new PathBuilder<>(entityClass, entityClass.getSimpleName().toLowerCase());

        // Filter deleted records
        if (!searchRequest.getIncludeDeleted()) {
            builder.and(entityPath.getBoolean("deleted").eq(false));
        }

        // Apply filters
        List<FilterRequest> filters = searchRequest.getFilters();
        if (filters != null && !filters.isEmpty()) {
            for (FilterRequest filter : filters) {
                Predicate predicate = buildFilterPredicate(filter, entityPath);
                if (predicate != null) {
                    if ("OR".equalsIgnoreCase(searchRequest.getLogic())) {
                        builder.or(predicate);
                    } else {
                        builder.and(predicate);
                    }
                }
            }
        }

        return builder;
    }

    private Predicate buildFilterPredicate(FilterRequest filter, PathBuilder<E> path) {
        try {
            String field = filter.getField();
            String operator = filter.getOperator().toLowerCase();
            Object value = filter.getValue();
            Object secondValue = filter.getSecondValue();

            return switch (operator) {
                case "eq", "=" -> buildEquals(path, field, value);
                case "ne", "!=" -> buildNotEquals(path, field, value);
                case "gt", ">" -> buildGreaterThan(path, field, value);
                case "gte", ">=" -> buildGreaterThanOrEqual(path, field, value);
                case "lt", "<" -> buildLessThan(path, field, value);
                case "lte", "<=" -> buildLessThanOrEqual(path, field, value);
                case "like", "contains" -> buildContains(path, field, value);
                case "starts_with" -> buildStartsWith(path, field, value);
                case "ends_with" -> buildEndsWith(path, field, value);
                case "in" -> buildIn(path, field, value);
                case "not_in" -> buildNotIn(path, field, value);
                case "between" -> buildBetween(path, field, value, secondValue);
                case "is_null" -> buildIsNull(path, field);
                case "is_not_null" -> buildIsNotNull(path, field);
                default -> null;
            };
        } catch (Exception e) {
            log.error("Error building filter predicate: {}", e.getMessage());
            return null;
        }
    }

    private Predicate buildEquals(PathBuilder<E> path, String field, Object value) {
        if (value instanceof Integer) {
            return path.getNumber(field, Integer.class).eq((Integer) value);
        } else if (value instanceof Long) {
            return path.getNumber(field, Long.class).eq((Long) value);
        } else if (value instanceof Double) {
            return path.getNumber(field, Double.class).eq((Double) value);
        } else if (value instanceof Float) {
            return path.getNumber(field, Float.class).eq((Float) value);
        } else if (value instanceof Boolean) {
            return path.getBoolean(field).eq((Boolean) value);
        } else if (value instanceof LocalDateTime) {
            return path.getDateTime(field, LocalDateTime.class).eq((LocalDateTime) value);
        } else if (value instanceof LocalDate) {
            return path.getDate(field, LocalDate.class).eq((LocalDate) value);
        }
        return path.getString(field).eq(value.toString());
    }

    private Predicate buildNotEquals(PathBuilder<E> path, String field, Object value) {
        if (value instanceof Integer) {
            return path.getNumber(field, Integer.class).ne((Integer) value);
        } else if (value instanceof Long) {
            return path.getNumber(field, Long.class).ne((Long) value);
        } else if (value instanceof Double) {
            return path.getNumber(field, Double.class).ne((Double) value);
        } else if (value instanceof Float) {
            return path.getNumber(field, Float.class).ne((Float) value);
        } else if (value instanceof Boolean) {
            return path.getBoolean(field).ne((Boolean) value);
        }
        return path.getString(field).ne(value.toString());
    }

    private Predicate buildGreaterThan(PathBuilder<E> path, String field, Object value) {
        if (value instanceof Integer) {
            return path.getNumber(field, Integer.class).gt((Integer) value);
        } else if (value instanceof Long) {
            return path.getNumber(field, Long.class).gt((Long) value);
        } else if (value instanceof Double) {
            return path.getNumber(field, Double.class).gt((Double) value);
        } else if (value instanceof Float) {
            return path.getNumber(field, Float.class).gt((Float) value);
        } else if (value instanceof LocalDateTime) {
            return path.getDateTime(field, LocalDateTime.class).gt((LocalDateTime) value);
        } else if (value instanceof LocalDate) {
            return path.getDate(field, LocalDate.class).gt((LocalDate) value);
        }
        return null;
    }

    private Predicate buildGreaterThanOrEqual(PathBuilder<E> path, String field, Object value) {
        if (value instanceof Integer) {
            return path.getNumber(field, Integer.class).goe((Integer) value);
        } else if (value instanceof Long) {
            return path.getNumber(field, Long.class).goe((Long) value);
        } else if (value instanceof Double) {
            return path.getNumber(field, Double.class).goe((Double) value);
        } else if (value instanceof Float) {
            return path.getNumber(field, Float.class).goe((Float) value);
        } else if (value instanceof LocalDateTime) {
            return path.getDateTime(field, LocalDateTime.class).goe((LocalDateTime) value);
        } else if (value instanceof LocalDate) {
            return path.getDate(field, LocalDate.class).goe((LocalDate) value);
        }
        return null;
    }

    private Predicate buildLessThan(PathBuilder<E> path, String field, Object value) {
        if (value instanceof Integer) {
            return path.getNumber(field, Integer.class).lt((Integer) value);
        } else if (value instanceof Long) {
            return path.getNumber(field, Long.class).lt((Long) value);
        } else if (value instanceof Double) {
            return path.getNumber(field, Double.class).lt((Double) value);
        } else if (value instanceof Float) {
            return path.getNumber(field, Float.class).lt((Float) value);
        } else if (value instanceof LocalDateTime) {
            return path.getDateTime(field, LocalDateTime.class).lt((LocalDateTime) value);
        } else if (value instanceof LocalDate) {
            return path.getDate(field, LocalDate.class).lt((LocalDate) value);
        }
        return null;
    }

    private Predicate buildLessThanOrEqual(PathBuilder<E> path, String field, Object value) {
        if (value instanceof Integer) {
            return path.getNumber(field, Integer.class).loe((Integer) value);
        } else if (value instanceof Long) {
            return path.getNumber(field, Long.class).loe((Long) value);
        } else if (value instanceof Double) {
            return path.getNumber(field, Double.class).loe((Double) value);
        } else if (value instanceof Float) {
            return path.getNumber(field, Float.class).loe((Float) value);
        } else if (value instanceof LocalDateTime) {
            return path.getDateTime(field, LocalDateTime.class).loe((LocalDateTime) value);
        } else if (value instanceof LocalDate) {
            return path.getDate(field, LocalDate.class).loe((LocalDate) value);
        }
        return null;
    }

    private Predicate buildContains(PathBuilder<E> path, String field, Object value) {
        return path.getString(field).containsIgnoreCase(value.toString());
    }

    private Predicate buildStartsWith(PathBuilder<E> path, String field, Object value) {
        return path.getString(field).startsWithIgnoreCase(value.toString());
    }

    private Predicate buildEndsWith(PathBuilder<E> path, String field, Object value) {
        return path.getString(field).endsWithIgnoreCase(value.toString());
    }

    private Predicate buildIn(PathBuilder<E> path, String field, Object value) {
        if (value instanceof Collection) {
            return path.getString(field).in((Collection<String>) value);
        }
        return null;
    }

    private Predicate buildNotIn(PathBuilder<E> path, String field, Object value) {
        if (value instanceof Collection) {
            return path.getString(field).notIn((Collection<String>) value);
        }
        return null;
    }

    private Predicate buildBetween(PathBuilder<E> path, String field, Object from, Object to) {
        if (from instanceof Integer && to instanceof Integer) {
            return path.getNumber(field, Integer.class).between((Integer) from, (Integer) to);
        } else if (from instanceof Long && to instanceof Long) {
            return path.getNumber(field, Long.class).between((Long) from, (Long) to);
        } else if (from instanceof Double && to instanceof Double) {
            return path.getNumber(field, Double.class).between((Double) from, (Double) to);
        } else if (from instanceof Float && to instanceof Float) {
            return path.getNumber(field, Float.class).between((Float) from, (Float) to);
        } else if (from instanceof LocalDateTime && to instanceof LocalDateTime) {
            return path.getDateTime(field, LocalDateTime.class).between((LocalDateTime) from, (LocalDateTime) to);
        } else if (from instanceof LocalDate && to instanceof LocalDate) {
            return path.getDate(field, LocalDate.class).between((LocalDate) from, (LocalDate) to);
        }
        return null;
    }

    private Predicate buildIsNull(PathBuilder<E> path, String field) {
        return path.getString(field).isNull();
    }

    private Predicate buildIsNotNull(PathBuilder<E> path, String field) {
        return path.getString(field).isNotNull();
    }
}
