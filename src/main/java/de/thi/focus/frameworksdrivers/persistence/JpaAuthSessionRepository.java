package de.thi.focus.frameworksdrivers.persistence;

import de.thi.focus.frameworksdrivers.persistence.jpa.AuthSessionEntity;
import de.thi.focus.usecases.ports.outbound.auth.AuthSessionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class JpaAuthSessionRepository implements AuthSessionRepository {

    @PersistenceContext
    EntityManager em;

    @Transactional
    public void persist(AuthSessionEntity session) {
        em.persist(session);
    }

    public Optional<AuthSessionEntity> findActive(UUID id, Instant now) {
        List<AuthSessionEntity> res = em.createQuery("""
                select s from AuthSessionEntity s
                where s.id = :id
                  and s.revokedAt is null
                  and s.expiresAt > :now
                """, AuthSessionEntity.class)
                .setParameter("id", id)
                .setParameter("now", now)
                .setMaxResults(1)
                .getResultList();

        return res.isEmpty() ? Optional.empty() : Optional.of(res.get(0));
    }

    public Optional<AuthSessionEntity> findById(UUID id) {
        return Optional.ofNullable(em.find(AuthSessionEntity.class, id));
    }

    @Transactional
    public boolean revoke(UUID id, Instant revokedAt) {
        AuthSessionEntity s = em.find(AuthSessionEntity.class, id);
        if (s == null) return false;

        // idempotent revoke
        if (s.revokedAt == null) {
            s.revokedAt = revokedAt;
            s.lastSeenAt = revokedAt;
        }
        return true;
    }

    @Transactional
    public boolean touchLastSeen(UUID id, Instant now) {
        AuthSessionEntity s = em.find(AuthSessionEntity.class, id);
        if (s == null) return false;
        if (s.revokedAt != null) return false;

        s.lastSeenAt = now;
        return true;
    }
}
