package de.thi.focus.usecases.interactor;

import de.thi.focus.entities.Category;
import de.thi.focus.entities.valueobjects.Color;
import de.thi.focus.usecases.dtos.input.ChangeCategoryColorCommand;
import de.thi.focus.usecases.dtos.output.ChangeCategoryColorOutputDTO;
import de.thi.focus.usecases.errors.CategoryAccessDeniedException;
import de.thi.focus.usecases.errors.CategoryNotFoundException;
import de.thi.focus.usecases.factories.FocusValueObjectFactory;
import de.thi.focus.usecases.ports.inbound.ChangeCategoryColorInputPort;
import de.thi.focus.usecases.ports.outbound.CategoryRepository;
import de.thi.focus.usecases.ports.outbound.EventPublisher;

import java.util.List;
import java.util.Objects;

public final class ChangeCategoryColorInteractor implements ChangeCategoryColorInputPort {

    private final CategoryRepository categoryRepository;
    private final FocusValueObjectFactory voFactory;
    private final EventPublisher eventPublisher;

    public ChangeCategoryColorInteractor(
            CategoryRepository categoryRepository,
            FocusValueObjectFactory voFactory,
            EventPublisher eventPublisher
    ) {
        this.categoryRepository = Objects.requireNonNull(categoryRepository);
        this.voFactory = Objects.requireNonNull(voFactory);
        this.eventPublisher = Objects.requireNonNull(eventPublisher);
    }

    @Override
    public ChangeCategoryColorOutputDTO execute(ChangeCategoryColorCommand command) {

        Category category = categoryRepository.findById(command.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(command.categoryId()));

        if (!category.getOwner().equals(command.userId())) {
            throw new CategoryAccessDeniedException(command.userId(), command.categoryId());
        }

        Color newColor = voFactory.colorRequired(command.color());

        category.changeColor(newColor);

        categoryRepository.save(category);
        eventPublisher.publish(List.of());

        return new ChangeCategoryColorOutputDTO(category.getId());
    }
}
