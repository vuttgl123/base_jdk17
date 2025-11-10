package com.base.model.mapper;

import com.base.configuration.config.MapStructConfig;
import com.base.model.entity.User;
import com.base.model.response.auth.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        config = MapStructConfig.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {
    @Mapping(target = "roles", expression = "java(user.getRoles().stream().map(r -> r.getName().name()).collect(java.util.stream.Collectors.toSet()))")
    UserResponse toResponse(User user);
}
