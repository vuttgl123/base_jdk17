package com.base.ddd.shared.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Aggregate Root - điểm truy cập chính vào aggregate
 * Đảm bảo tính nhất quán của toàn bộ aggregate
 */
public abstract class AggregateRoot<ID extends Serializable> extends Entity<ID> {

    private transient final List<DomainEvent> domainEvents = new ArrayList<>();
    private boolean deleted = false;

    protected AggregateRoot() {
        super();
    }

    protected AggregateRoot(ID id) {
        super(id);
    }

    /**
     * Register a domain event
     */
    protected void registerEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }

    /**
     * Get all domain events (for publishing)
     */
    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    /**
     * Clear all domain events (after publishing)
     */
    public void clearDomainEvents() {
        this.domainEvents.clear();
    }

    /**
     * Soft delete
     */
    public void markAsDeleted() {
        if (this.deleted) {
            throw new IllegalStateException("Aggregate already deleted");
        }
        this.deleted = true;
    }

    /**
     * Restore from soft delete
     */
    public void restore() {
        if (!this.deleted) {
            throw new IllegalStateException("Aggregate is not deleted");
        }
        this.deleted = false;
    }

    public boolean isDeleted() {
        return deleted;
    }

    protected void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * Validate aggregate invariants
     * Override this method to add custom validation
     */
    protected void validate() {
        // Override in subclasses
    }
}