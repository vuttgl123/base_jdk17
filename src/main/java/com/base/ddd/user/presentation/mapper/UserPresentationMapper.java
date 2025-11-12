package com.base.ddd.user.presentation.mapper;

import com.base.ddd.user.application.dto.CreateUserCommand;
import com.base.ddd.user.application.dto.UpdateUserCommand;
import com.base.ddd.user.presentation.rest.request.CreateUserRequest;
import com.base.ddd.user.presentation.rest.request.UpdateUserRequest;
import org.springframework.stereotype.Component;

/**
 * Mapper between Presentation (API) and Application Layer
 */
@Component
public class UserPresentationMapper {

    public CreateUserCommand toCreateCommand(CreateUserRequest request) {
        return CreateUserCommand.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .age(request.getAge())
                .address(request.getAddress())
                .build();
    }

    public UpdateUserCommand toUpdateCommand(UpdateUserRequest request) {
        return UpdateUserCommand.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .age(request.getAge())
                .address(request.getAddress())
                .avatarUrl(request.getAvatarUrl())
                .status(request.getStatus())
                .build();
    }
}