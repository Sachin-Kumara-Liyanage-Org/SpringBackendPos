package com.techbirdssolutions.springpos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.time.LocalDateTime;
/**
 * Represents a string metadata type.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public abstract class Auditable {
    /**
     * The date and time when the entity was created.
     * It is not updatable after its initial creation.
     */
    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private Timestamp createdAt;
    /**
     * The user who created the entity.
     * It is not updatable after its initial creation.
     */
    @Column(name = "created_by",updatable = false)
    @CreatedBy
    private String createdBy;
    /**
     * The date and time when the entity was last updated.
     */
    @Column(name = "updated_at")
    @LastModifiedDate
    private Timestamp updatedAt;
    /**
     * The user who last updated the entity.
     */
    @Column(name = "updated_by")
    @LastModifiedBy
    private String updatedBy;
}
