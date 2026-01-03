package de.thi.focus.usecases.interactor;

import de.thi.focus.config.FocusConstraintsConfig;
import de.thi.focus.entities.Category;
import de.thi.focus.entities.valueobjects.CategoryName;
import de.thi.focus.usecases.dtos.input.RenameCategoryCommand;
import de.thi.focus.usecases.dtos.output.RenameCategoryOutputDTO;
import de.thi.focus.usecases.errors.CategoryAccessDeniedException;
import de.thi.focus.usecases.errors.CategoryNotFoundException;
import de.thi.focus.usecases.policies.UniqueCategoryNamePolicy;
import de.thi.focus.usecases.ports.inbound.RenameCategoryInputPort;
import de.thi.focus.usecases.ports.outbound.CategoryRepository;
import de.thi.focus.usecases.ports.outbound.EventPublisher;

import java.util.List;
import java.util.Objects;

public final class RenameCategoryInteractor implements RenameCategoryInputPort {

    private final CategoryRepository categoryRepository;
    private final UniqueCategoryNamePolicy uniqueCategoryNamePolicy;
    private final EventPublisher eventPublisher;
    private final FocusConstraintsConfig constraints;

    public RenameCategoryInteractor(
            CategoryRepository categoryRepository,
            UniqueCategoryNamePolicy uniqueCategoryNamePolicy,
            EventPublisher eventPublisher,
            FocusConstraintsConfig constraints
    ) {
        this.categoryRepository = Objects.requireNonNull(categoryRepository);
        this.uniqueCategoryNamePolicy = Objects.requireNonNull(uniqueCategoryNamePolicy);
        this.eventPublisher = Objects.requireNonNull(eventPublisher);
        this.constraints = Objects.requireNonNull(constraints);
    }

    @Override
    public RenameCategoryOutputDTO execute(RenameCategoryCommand command) {

        Category category = categoryRepository.findById(command.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(command.categoryId()));

        if (!category.getOwner().equals(command.userId())) {
            throw new CategoryAccessDeniedException(command.userId(), command.categoryId());
        }

        CategoryName newName = CategoryName.of(
                command.newName(),
                constraints.category().name().maxLength()
        );

        // Application rule: unique per user
        uniqueCategoryNamePolicy.ensureUnique(command.userId(), newName, category.getId());

        // Domain operation
        category.rename(newName);

        // Persist
        categoryRepository.save(category);

        // Publish events (enable when you actually collect events in entities)
        eventPublisher.publish(List.of());

        return new RenameCategoryOutputDTO(category.getId());
    }
}
