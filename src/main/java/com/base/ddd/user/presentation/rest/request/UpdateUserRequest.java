package com.base.ddd.user.presentation.rest.request;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * Update User Request (API Layer)
 */
@Data
public class UpdateUserRequest {

    @Email(message = "Email không đúng định dạng")
    @Size(max = 100)
    private String email;

    @Size(max = 100)
    private String fullName;

    @Pattern(regexp = "^[0-9+\\-\\s()]*$", message = "Số điện thoại không hợp lệ")
    @Size(max = 20)
    private String phoneNumber;

    @Min(value = 1)
    @Max(value = 150)
    private Integer age;

    private String address;

    private String avatarUrl;

    @Pattern(regexp = "^(ACTIVE|INACTIVE|BLOCKED|PENDING)$",
            message = "Status must be ACTIVE, INACTIVE, BLOCKED, or PENDING")
    private String status;
}