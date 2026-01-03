package de.thi.focus.usecases.interactor;

import de.thi.focus.entities.Category;
import de.thi.focus.usecases.dtos.input.DeleteCategoryCommand;
import de.thi.focus.usecases.dtos.output.DeleteCategoryOutputDTO;
import de.thi.focus.usecases.errors.CategoryAccessDeniedException;
import de.thi.focus.usecases.errors.CategoryInUseException;
import de.thi.focus.usecases.errors.CategoryNotFoundException;
import de.thi.focus.usecases.ports.inbound.DeleteCategoryInputPort;
import de.thi.focus.usecases.ports.outbound.CategoryRepository;
import de.thi.focus.usecases.ports.outbound.EventPublisher;
import de.thi.focus.usecases.ports.outbound.FocusSessionRepository;

import java.util.List;
import java.util.Objects;

public final class DeleteCategoryInteractor implements DeleteCategoryInputPort {

    private final CategoryRepository categoryRepository;
    private final FocusSessionRepository sessionRepository;
    private final EventPublisher eventPublisher;

    public DeleteCategoryInteractor(
            CategoryRepository categoryRepository,
            FocusSessionRepository sessionRepository,
            EventPublisher eventPublisher
    ) {
        this.categoryRepository = Objects.requireNonNull(categoryRepository);
        this.sessionRepository = Objects.requireNonNull(sessionRepository);
        this.eventPublisher = Objects.requireNonNull(eventPublisher);
    }

    @Override
    public DeleteCategoryOutputDTO execute(DeleteCategoryCommand command) {

        Category category = categoryRepository.findById(command.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(command.categoryId()));

        if (!category.getOwner().equals(command.userId())) {
            throw new CategoryAccessDeniedException(command.userId(), command.categoryId());
        }

        boolean isUsed = sessionRepository.existsByOwnerAndCategoryId(command.userId(), command.categoryId());
        if (isUsed) {
            throw new CategoryInUseException(command.categoryId());
        }

        categoryRepository.deleteById(command.categoryId());

        eventPublisher.publish(List.of());

        return new DeleteCategoryOutputDTO(command.categoryId());
    }
}
