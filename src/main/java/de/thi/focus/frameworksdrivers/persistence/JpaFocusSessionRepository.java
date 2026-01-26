package de.thi.focus.frameworksdrivers.persistence;

import de.thi.focus.entities.FocusSession;
import de.thi.focus.entities.ids.CategoryId;
import de.thi.focus.entities.ids.FocusSessionId;
import de.thi.focus.entities.ids.UserId;
import de.thi.focus.frameworksdrivers.persistence.jpa.FocusSessionEntity;
import de.thi.focus.usecases.factories.FocusValueObjectFactory;
import de.thi.focus.usecases.ports.outbound.FocusSessionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JpaFocusSessionRepository implements FocusSessionRepository {

    @Inject
    EntityManager em;

    @Inject
    FocusValueObjectFactory voFactory;

    @Override
    public Optional<FocusSession> findRunningByUser(UserId userId) {
        var list = em.createQuery("""
                select s from FocusSessionEntity s
                where s.ownerId = :ownerId and s.endAt is null
                order by s.startAt desc
                """, FocusSessionEntity.class)
                .setParameter("ownerId", userId.value())
                .setMaxResults(1)
                .getResultList();

        return list.stream().findFirst().map(e -> FocusSessionMapper.toDomain(e, voFactory));
    }

    @Override
    public Optional<FocusSession> findById(FocusSessionId id) {
        var e = em.find(FocusSessionEntity.class, id.value());
        return Optional.ofNullable(e).map(entity -> FocusSessionMapper.toDomain(entity, voFactory));
    }

    @Override
    public Optional<FocusSession> findLastFinishedByUser(UserId userId) {
        var list = em.createQuery("""
                select s from FocusSessionEntity s
                where s.ownerId = :ownerId and s.endAt is not null
                order by s.endAt desc
                """, FocusSessionEntity.class)
                .setParameter("ownerId", userId.value())
                .setMaxResults(1)
                .getResultList();

        return list.stream().findFirst().map(e -> FocusSessionMapper.toDomain(e, voFactory));
    }

    @Override
    @Transactional
    public void save(FocusSession session) {
        FocusSessionEntity existing = em.find(FocusSessionEntity.class, session.getId().value());
        
        if (existing == null) {
            em.persist(FocusSessionMapper.toEntity(session));
        } else {
            existing.ownerId = session.getOwner().value();
            existing.startAt = session.getTimeRange().getStart();
            existing.endAt = session.getTimeRange().getEnd();
            existing.categoryId = session.getCategoryId() != null ? session.getCategoryId().value() : null;
            existing.note = session.getNote() != null ? session.getNote().toString() : null;
            // createdAt and updatedAt are managed by @PrePersist/@PreUpdate
        }
    }

    @Override
    public boolean existsByOwnerAndCategoryId(UserId ownerId, CategoryId categoryId) {
        var count = em.createQuery("""
                select count(s) from FocusSessionEntity s
                where s.ownerId = :ownerId and s.categoryId = :categoryId
                """, Long.class)
                .setParameter("ownerId", ownerId.value())
                .setParameter("categoryId", categoryId.value())
                .getSingleResult();

        return count != null && count > 0;
    }

    @Override
    public List<FocusSession> findAllByUser(UserId userId) {
        return em.createQuery("""
                select s from FocusSessionEntity s
                where s.ownerId = :ownerId
                order by s.startAt desc
                """, FocusSessionEntity.class)
                .setParameter("ownerId", userId.value())
                .getResultList()
                .stream()
                .map(e -> FocusSessionMapper.toDomain(e, voFactory))
                .toList();
    }
}
