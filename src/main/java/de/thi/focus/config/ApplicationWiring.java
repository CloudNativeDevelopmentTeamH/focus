package de.thi.focus.config;

import de.thi.focus.frameworksdrivers.persistence.*;
import de.thi.focus.interfaceadapters.web.presenter.CategoryHttpPresenter;
import de.thi.focus.interfaceadapters.web.presenter.JsonCategoryHttpPresenter;
import de.thi.focus.interfaceadapters.web.presenter.JsonSessionHttpPresenter;
import de.thi.focus.interfaceadapters.web.presenter.SessionHttpPresenter;

import de.thi.focus.usecases.factories.FocusValueObjectFactory;
import de.thi.focus.usecases.interactor.*;

import de.thi.focus.usecases.policies.RunningSessionPolicy;

import de.thi.focus.usecases.policies.UniqueCategoryNamePolicy;
import de.thi.focus.usecases.ports.inbound.*;

import de.thi.focus.usecases.ports.outbound.CategoryRepository;
import de.thi.focus.usecases.ports.outbound.system.Clock;
import de.thi.focus.usecases.ports.outbound.EventPublisher;
import de.thi.focus.usecases.ports.outbound.FocusSessionRepository;

import de.thi.focus.frameworksdrivers.events.NoopEventPublisher;
import de.thi.focus.frameworksdrivers.time.SystemClock;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class ApplicationWiring {

    // ---------- Interface Adapters ----------
    @Produces
    @ApplicationScoped
    SessionHttpPresenter sessionHttpPresenter() {
        return new JsonSessionHttpPresenter();
    }

    @Produces
    @ApplicationScoped
    CategoryHttpPresenter categoryHttpPresenter() {
        return new JsonCategoryHttpPresenter();
    }

    // ---------- Outbound adapters ----------

    @Produces
    public CategoryRepository categoryRepository(EntityManager em, FocusValueObjectFactory voFactory) {
        return new JpaCategoryRepository(em, voFactory);
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

    // ---------- Factories ----------
    @Produces
    @ApplicationScoped
    FocusValueObjectFactory focusValueObjectFactory(
            FocusConstraintsConfig constraints,
            FocusDefaultsConfig defaults
    ) {
        return new FocusValueObjectFactory(constraints, defaults);
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
    GetRunningSessionInputPort getRunningSessionInputPort(FocusSessionRepository sessionRepository) {
        return new GetRunningSessionInteractor(sessionRepository);
    }

    @Produces
    @ApplicationScoped
    UpdateSessionInputPort updateSessionInputPort(
            FocusSessionRepository sessionRepository,
            FocusValueObjectFactory voFactory,
            EventPublisher eventPublisher
    ) {
        return new UpdateSessionInteractor(sessionRepository, voFactory, eventPublisher);
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


    @Produces
    @ApplicationScoped
    CreateCategoryInputPort createCategoryInputPort(
            CategoryRepository categoryRepository,
            FocusValueObjectFactory voFactory,
            EventPublisher eventPublisher
    ) {
        return new CreateCategoryInteractor(categoryRepository, voFactory, eventPublisher);
    }

    @Produces
    @ApplicationScoped
    ArchiveCategoryInputPort archiveCategoryInputPort(
            CategoryRepository categoryRepository,
            EventPublisher eventPublisher
    ) {
        return new ArchiveCategoryInteractor(categoryRepository, eventPublisher);
    }

    @Produces
    @ApplicationScoped
    UnarchiveCategoryInputPort unarchiveCategoryInputPort(
            CategoryRepository categoryRepository,
            EventPublisher eventPublisher
    ) {
        return new UnarchiveCategoryInteractor(categoryRepository, eventPublisher);
    }

    @Produces
    @ApplicationScoped
    DeleteCategoryInputPort deleteCategoryInputPort(
            CategoryRepository categoryRepository,
            FocusSessionRepository sessionRepository,
            EventPublisher eventPublisher
    ) {
        return new DeleteCategoryInteractor(categoryRepository, sessionRepository, eventPublisher);
    }

    @Produces
    @ApplicationScoped
    ListCategoriesInputPort listCategoriesInputPort(CategoryRepository categoryRepository) {
        return new ListCategoriesInteractor(categoryRepository);
    }

    @Produces
    @ApplicationScoped
    ChangeCategoryColorInputPort changeCategoryColorInputPort(
            CategoryRepository categoryRepository,
            FocusValueObjectFactory voFactory,
            EventPublisher eventPublisher
    ) {
        return new ChangeCategoryColorInteractor(categoryRepository, voFactory, eventPublisher);
    }
}
