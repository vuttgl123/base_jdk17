package com.base.querydsl;


import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper extends GenericMapper<User, UserCreateRequest, UserUpdateRequest, UserResponse> {

    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "status", constant = "ACTIVE")
    @Override
    User toEntity(UserCreateRequest createRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Override
    void updateEntity(UserUpdateRequest updateRequest, @MappingTarget User entity);
}
