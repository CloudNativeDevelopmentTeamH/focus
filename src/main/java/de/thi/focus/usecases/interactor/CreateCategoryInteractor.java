package de.thi.focus.usecases.interactor;

import de.thi.focus.entities.Category;
import de.thi.focus.entities.ids.CategoryId;
import de.thi.focus.entities.valueobjects.CategoryName;
import de.thi.focus.usecases.dtos.input.CreateCategoryCommand;
import de.thi.focus.usecases.dtos.output.CreateCategoryOutputDTO;
import de.thi.focus.usecases.errors.CategoryNameAlreadyExistsException;
import de.thi.focus.usecases.factories.FocusValueObjectFactory;
import de.thi.focus.usecases.ports.inbound.CreateCategoryInputPort;
import de.thi.focus.usecases.ports.outbound.CategoryRepository;
import de.thi.focus.usecases.ports.outbound.EventPublisher;
import de.thi.focus.entities.valueobjects.Color;

import java.util.List;
import java.util.Objects;

public final class CreateCategoryInteractor implements CreateCategoryInputPort {

    private final CategoryRepository categoryRepository;
    private final FocusValueObjectFactory voFactory;
    private final EventPublisher eventPublisher;

    public CreateCategoryInteractor(
            CategoryRepository categoryRepository,
            FocusValueObjectFactory voFactory,
            EventPublisher eventPublisher
    ) {
        this.categoryRepository = Objects.requireNonNull(categoryRepository);
        this.voFactory = Objects.requireNonNull(voFactory);
        this.eventPublisher = Objects.requireNonNull(eventPublisher);
    }

    @Override
    public CreateCategoryOutputDTO execute(CreateCategoryCommand command) {

        CategoryName name = voFactory.categoryName(command.name());

        Color color = voFactory.colorOrDefault(command.color());

        categoryRepository.findByOwnerAndName(command.userId(), name)
                .ifPresent(existing -> { throw new CategoryNameAlreadyExistsException(name); });

        Category category = Category.create(
                CategoryId.newId(),
                command.userId(),
                name,
                color
        );

        categoryRepository.save(category);
        eventPublisher.publish(List.of());

        return new CreateCategoryOutputDTO(category.getId());
    }
}
