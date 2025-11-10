package com.base.model.entity;

import com.base.model.enumeration.EventType;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@Entity
@Table(name = "events",
        indexes = {
                @Index(name = "ix_events_time", columnList = "occurred_at"),
                @Index(name = "ix_events_type", columnList = "event_type"),
                @Index(name = "ix_events_user", columnList = "user_id"),
                @Index(name = "ix_events_lead", columnList = "lead_id"),
                @Index(name = "ix_events_campaign", columnList = "campaign_id")
        })
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Event extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",
            foreignKey = @ForeignKey(name = "fk_events_user"))
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id",
            foreignKey = @ForeignKey(name = "fk_events_lead"))
    @ToString.Exclude
    private Lead lead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id",
            foreignKey = @ForeignKey(name = "fk_events_session"))
    @ToString.Exclude
    private Session session;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 40)
    private EventType eventType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id",
            foreignKey = @ForeignKey(name = "fk_events_product"))
    @ToString.Exclude
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id",
            foreignKey = @ForeignKey(name = "fk_events_campaign"))
    @ToString.Exclude
    private Campaign campaign;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSON")
    private JsonNode meta;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;
}
