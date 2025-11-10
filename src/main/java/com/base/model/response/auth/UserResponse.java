package com.base.model.response.auth;

import com.base.model.enumeration.UserStatus;

import java.util.Set;

public record UserResponse(
        Long id,
        String email,
        UserStatus status,
        Set<String> roles
) {}
