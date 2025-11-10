package com.base.model.entity;

import com.base.model.enumeration.DiscountType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "coupons",
        indexes = @Index(name = "ix_coupons_campaign", columnList = "campaign_id"),
        uniqueConstraints = @UniqueConstraint(name = "uk_coupons_code", columnNames = "code"))
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Coupon extends BaseEntity {

    @Column(nullable = false, length = 64)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false, length = 10)
    private DiscountType discountType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal value;

    @Column(name = "start_at")
    private Instant startAt;

    @Column(name = "end_at")
    private Instant endAt;

    @Column(name = "max_uses")
    private Integer maxUses;

    @Column(name = "used_count", nullable = false)
    private Integer usedCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id",
            foreignKey = @ForeignKey(name = "fk_coupons_campaign"))
    @ToString.Exclude
    private Campaign campaign;
}
