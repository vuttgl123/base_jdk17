package com.base.querydsl;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class BaseResponse {
    private Long id;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private Long version;
}
