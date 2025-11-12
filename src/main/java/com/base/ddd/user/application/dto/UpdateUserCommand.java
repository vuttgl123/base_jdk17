package com.base.ddd.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Update User Command
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserCommand {
    private String email;
    private String fullName;
    private String phoneNumber;
    private Integer age;
    private String address;
    private String avatarUrl;
    private String status;
}