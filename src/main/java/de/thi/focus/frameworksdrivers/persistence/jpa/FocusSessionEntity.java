package de.thi.focus.frameworksdrivers.persistence.jpa;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "focus_sessions")
public class FocusSessionEntity {

    @Id
    @Column(name = "id", nullable = false)
    public UUID id;

    @Column(name = "owner_id", nullable = false)
    public int ownerId;

    @Column(name = "start_at", nullable = false)
    public Instant startAt;

    @Column(name = "end_at")
    public Instant endAt;

    @Column(name = "category_id")
    public UUID categoryId;

    @Column(name = "note")
    public String note;

    @Column(name = "created_at", nullable = false)
    public Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    public Instant updatedAt;

    @PrePersist
    void prePersist() {
        var now = Instant.now();
        if (createdAt == null) createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
    }
}
