package com.base.querydsl;

import org.mapstruct.MappingTarget;

import java.util.List;

public interface GenericMapper<E, CREATE_REQ, UPDATE_REQ, RES> {

    RES toResponse(E entity);

    E toEntity(CREATE_REQ createRequest);

    List<RES> toResponseList(List<E> entities);

    void updateEntity(UPDATE_REQ updateRequest, @MappingTarget E entity);
}
