package de.thi.focus.config;

import de.thi.focus.interfaceadapters.web.presenter.JsonSessionHttpPresenter;
import de.thi.focus.interfaceadapters.web.presenter.SessionHttpPresenter;

import de.thi.focus.usecases.interactor.RenameCategoryInteractor;
import de.thi.focus.usecases.interactor.ResumeSessionInteractor;
import de.thi.focus.usecases.interactor.StartSessionInteractor;
import de.thi.focus.usecases.interactor.StopSessionInteractor;

import de.thi.focus.usecases.policies.RunningSessionPolicy;

import de.thi.focus.usecases.policies.UniqueCategoryNamePolicy;
import de.thi.focus.usecases.ports.inbound.RenameCategoryInputPort;
import de.thi.focus.usecases.ports.inbound.ResumeSessionInputPort;
import de.thi.focus.usecases.ports.inbound.StartSessionInputPort;
import de.thi.focus.usecases.ports.inbound.StopSessionInputPort;

import de.thi.focus.usecases.ports.outbound.CategoryRepository;
import de.thi.focus.usecases.ports.outbound.system.Clock;
import de.thi.focus.usecases.ports.outbound.EventPublisher;
import de.thi.focus.usecases.ports.outbound.FocusSessionRepository;

import de.thi.focus.frameworksdrivers.events.NoopEventPublisher;
import de.thi.focus.frameworksdrivers.persistence.InMemoryCategoryRepository;
import de.thi.focus.frameworksdrivers.persistence.InMemoryFocusSessionRepository;
import de.thi.focus.frameworksdrivers.time.SystemClock;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class ApplicationWiring {

    // ---------- Interface Adapters ----------
    @Produces
    @ApplicationScoped
    SessionHttpPresenter sessionHttpPresenter() {
        return new JsonSessionHttpPresenter();
    }

    // ---------- Outbound adapters (temporary in-memory) ----------
    @Produces
    @ApplicationScoped
    FocusSessionRepository focusSessionRepository() {
        return new InMemoryFocusSessionRepository();
    }

    @Produces
    @ApplicationScoped
    CategoryRepository categoryRepository() {
        return new InMemoryCategoryRepository();
    }

    @Produces
    @ApplicationScoped
    Clock clock() {
        return new SystemClock();
    }

    @Produces
    @ApplicationScoped
    EventPublisher eventPublisher() {
        return new NoopEventPublisher();
    }

    // ---------- Policies ----------
    @Produces
    @ApplicationScoped
    RunningSessionPolicy runningSessionPolicy(FocusSessionRepository sessionRepository) {
        return new RunningSessionPolicy(sessionRepository);
    }

    @Produces
    @ApplicationScoped
    UniqueCategoryNamePolicy uniqueCategoryNamePolicy(CategoryRepository categoryRepository) {
        return new UniqueCategoryNamePolicy(categoryRepository);
    }

    // ---------- Inbound ports (Interactors) ----------
    @Produces
    @ApplicationScoped
    StartSessionInputPort startSessionInputPort(
            FocusSessionRepository sessionRepository,
            CategoryRepository categoryRepository,
            RunningSessionPolicy runningSessionPolicy,
            Clock clock,
            EventPublisher eventPublisher,
            FocusConstraintsConfig constraints
    ) {
        return new StartSessionInteractor(
                sessionRepository,
                categoryRepository,
                runningSessionPolicy,
                clock,
                eventPublisher,
                constraints
        );
    }

    @Produces
    @ApplicationScoped
    StopSessionInputPort stopSessionInputPort(
            FocusSessionRepository sessionRepository,
            Clock clock,
            EventPublisher eventPublisher
    ) {
        return new StopSessionInteractor(sessionRepository, clock, eventPublisher);
    }

    @Produces
    @ApplicationScoped
    ResumeSessionInputPort resumeSessionInputPort(
            FocusSessionRepository sessionRepository,
            RunningSessionPolicy runningSessionPolicy,
            Clock clock,
            EventPublisher eventPublisher
    ) {
        return new ResumeSessionInteractor(sessionRepository, runningSessionPolicy, clock, eventPublisher);
    }

    @Produces
    @ApplicationScoped
    RenameCategoryInputPort renameCategoryInputPort(
            CategoryRepository categoryRepository,
            UniqueCategoryNamePolicy uniqueCategoryNamePolicy,
            EventPublisher eventPublisher,
            FocusConstraintsConfig constraints
    ) {
        return new RenameCategoryInteractor(
                categoryRepository,
                uniqueCategoryNamePolicy,
                eventPublisher,
                constraints
        );
    }
}
