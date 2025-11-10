package com.base.model.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "auth_providers",
        uniqueConstraints = @UniqueConstraint(name = "uq_provider_user",
                columnNames = {"provider", "provider_user_id"}),
        indexes = @Index(name = "ix_authprov_user", columnList = "user_id"))
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AuthProvider extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_authprov_user"))
    @ToString.Exclude
    private User user;

    @Column(nullable = false, length = 30)
    private String provider;

    @Column(name = "provider_user_id", nullable = false, length = 191)
    private String providerUserId;
}
