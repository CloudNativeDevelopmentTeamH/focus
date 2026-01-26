package de.thi.focus.usecases.ports.outbound.auth;

import de.thi.focus.frameworksdrivers.persistence.jpa.AuthSessionEntity;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Port for managing authentication sessions.
 * This interface defines the contract for session persistence and retrieval,
 * allowing the use cases layer to remain independent of the persistence implementation.
 */
public interface AuthSessionRepository {

    /**
     * Persists a new authentication session.
     *
     * @param session the session entity to persist
     */
    void persist(AuthSessionEntity session);

    /**
     * Finds an active (non-revoked and non-expired) session by ID.
     *
     * @param id  the session ID
     * @param now the current timestamp to check expiration against
     * @return Optional containing the session if found and active, empty otherwise
     */
    Optional<AuthSessionEntity> findActive(UUID id, Instant now);

    /**
     * Finds a session by ID regardless of its status.
     *
     * @param id the session ID
     * @return Optional containing the session if found, empty otherwise
     */
    Optional<AuthSessionEntity> findById(UUID id);

    /**
     * Revokes a session by setting its revokedAt timestamp.
     * This operation is idempotent.
     *
     * @param id        the session ID to revoke
     * @param revokedAt the revocation timestamp
     * @return true if the session was found, false otherwise
     */
    boolean revoke(UUID id, Instant revokedAt);

    /**
     * Updates the lastSeenAt timestamp of an active session.
     *
     * @param id  the session ID
     * @param now the current timestamp
     * @return true if the session was found and is still active, false otherwise
     */
    boolean touchLastSeen(UUID id, Instant now);
}
