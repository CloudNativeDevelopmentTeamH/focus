package de.thi.focus.frameworksdrivers.persistence.jpa;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "categories",
        uniqueConstraints = @UniqueConstraint(name = "ux_categories_owner_name", columnNames = {"owner_id", "name"})
)
public class CategoryEntity {

    @Id
    @Column(name = "id", nullable = false)
    public UUID id;

    @Column(name = "owner_id", nullable = false)
    public int ownerId;

    @Column(name = "name", nullable = false)
    public String name;

    @Column(name = "color")
    public String color;

    @Column(name = "archived", nullable = false)
    public boolean archived;

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
