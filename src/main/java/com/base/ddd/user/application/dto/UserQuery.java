package com.base.ddd.user.application.dto;

import com.base.ddd.shared.infrastructure.SearchRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * User Query
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserQuery extends SearchRequest {

    private String username;
    private String email;
    private String fullName;
    private String status;
    private Integer minAge;
    private Integer maxAge;
    private String phoneNumber;
    private Boolean deleted;

    private String createdBy;
    private String updatedBy;
}