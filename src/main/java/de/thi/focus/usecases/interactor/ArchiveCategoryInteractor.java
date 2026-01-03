package de.thi.focus.usecases.interactor;

import de.thi.focus.entities.Category;
import de.thi.focus.usecases.dtos.input.ArchiveCategoryCommand;
import de.thi.focus.usecases.dtos.output.ArchiveCategoryOutputDTO;
import de.thi.focus.usecases.errors.CategoryAccessDeniedException;
import de.thi.focus.usecases.errors.CategoryNotFoundException;
import de.thi.focus.usecases.ports.inbound.ArchiveCategoryInputPort;
import de.thi.focus.usecases.ports.outbound.CategoryRepository;
import de.thi.focus.usecases.ports.outbound.EventPublisher;

import java.util.List;
import java.util.Objects;

public final class ArchiveCategoryInteractor implements ArchiveCategoryInputPort {

    private final CategoryRepository categoryRepository;
    private final EventPublisher eventPublisher;

    public ArchiveCategoryInteractor(CategoryRepository categoryRepository, EventPublisher eventPublisher) {
        this.categoryRepository = Objects.requireNonNull(categoryRepository);
        this.eventPublisher = Objects.requireNonNull(eventPublisher);
    }

    @Override
    public ArchiveCategoryOutputDTO execute(ArchiveCategoryCommand command) {

        Category category = categoryRepository.findById(command.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(command.categoryId()));

        if (!category.getOwner().equals(command.userId())) {
            throw new CategoryAccessDeniedException(command.userId(), command.categoryId());
        }

        category.archive();

        categoryRepository.save(category);
        eventPublisher.publish(List.of());

        return new ArchiveCategoryOutputDTO(category.getId());
    }
}
