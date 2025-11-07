package com.base.model.mapper;

public interface BaseMapper<T, RQ, RS> {
    T toEntity(RQ request);
    void updateEntity(T entity, RQ request);
    RS toResponse(T entity);
}
