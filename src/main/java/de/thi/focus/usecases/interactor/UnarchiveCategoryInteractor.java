package de.thi.focus.usecases.interactor;

import de.thi.focus.entities.Category;
import de.thi.focus.usecases.dtos.input.UnarchiveCategoryCommand;
import de.thi.focus.usecases.dtos.output.UnarchiveCategoryOutputDTO;
import de.thi.focus.usecases.errors.CategoryAccessDeniedException;
import de.thi.focus.usecases.errors.CategoryNotFoundException;
import de.thi.focus.usecases.ports.inbound.UnarchiveCategoryInputPort;
import de.thi.focus.usecases.ports.outbound.CategoryRepository;
import de.thi.focus.usecases.ports.outbound.EventPublisher;

import java.util.List;
import java.util.Objects;

public final class UnarchiveCategoryInteractor implements UnarchiveCategoryInputPort {

    private final CategoryRepository categoryRepository;
    private final EventPublisher eventPublisher;

    public UnarchiveCategoryInteractor(CategoryRepository categoryRepository, EventPublisher eventPublisher) {
        this.categoryRepository = Objects.requireNonNull(categoryRepository);
        this.eventPublisher = Objects.requireNonNull(eventPublisher);
    }

    @Override
    public UnarchiveCategoryOutputDTO execute(UnarchiveCategoryCommand command) {

        Category category = categoryRepository.findById(command.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(command.categoryId()));

        if (!category.getOwner().equals(command.userId())) {
            throw new CategoryAccessDeniedException(command.userId(), command.categoryId());
        }

        category.unarchive();

        categoryRepository.save(category);
        eventPublisher.publish(List.of());

        return new UnarchiveCategoryOutputDTO(category.getId());
    }
}
