package com.base.ddd.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Create User Command
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserCommand {
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String phoneNumber;
    private Integer age;
    private String address;
}