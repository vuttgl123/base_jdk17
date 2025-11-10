package com.base.model.entity;

import com.base.model.enumeration.SubscriptionChannel;
import com.base.model.enumeration.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "subscriptions",
        indexes = {
                @Index(name = "ix_subs_user", columnList = "user_id"),
                @Index(name = "ix_subs_lead", columnList = "lead_id")
        })
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Subscription extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",
            foreignKey = @ForeignKey(name = "fk_subs_user"))
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id",
            foreignKey = @ForeignKey(name = "fk_subs_lead"))
    @ToString.Exclude
    private Lead lead;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private SubscriptionChannel channel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private SubscriptionStatus status;

}
