package com.base.model.mapper;

import org.mapstruct.MappingTarget;

public interface BaseMapper<T, RQ, RS> {
    T toEntity(RQ request);
    void updateEntity(@MappingTarget T entity, RQ request);
    RS toResponse(T entity);
}
