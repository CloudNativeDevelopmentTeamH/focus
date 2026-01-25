package de.thi.focus.frameworksdrivers.persistence.jpa;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "auth_sessions")
public class AuthSessionEntity {

    @Id
    @Column(name = "id", nullable = false)
    public UUID id;

    @Column(name = "user_id", nullable = false)
    public Integer userId;

    @Column(name = "token", nullable = false, columnDefinition = "text")
    public String token;

    @Column(name = "expires_at", nullable = false)
    public Instant expiresAt;

    @Column(name = "created_at", nullable = false)
    public Instant createdAt;

    @Column(name = "last_seen_at", nullable = false)
    public Instant lastSeenAt;

    @Column(name = "revoked_at")
    public Instant revokedAt;
}
