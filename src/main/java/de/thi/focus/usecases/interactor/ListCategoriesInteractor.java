package de.thi.focus.usecases.interactor;

import de.thi.focus.entities.Category;
import de.thi.focus.entities.ids.UserId;
import de.thi.focus.usecases.dtos.output.ListCategoriesOutputDTO;
import de.thi.focus.usecases.ports.inbound.ListCategoriesInputPort;
import de.thi.focus.usecases.ports.outbound.CategoryRepository;

import java.util.List;
import java.util.Objects;

public final class ListCategoriesInteractor implements ListCategoriesInputPort {

    private final CategoryRepository categoryRepository;

    public ListCategoriesInteractor(CategoryRepository categoryRepository) {
        this.categoryRepository = Objects.requireNonNull(categoryRepository);
    }

    @Override
    public ListCategoriesOutputDTO execute(UserId userId) {
        List<Category> categories = categoryRepository.findAllByOwner(userId);

        List<ListCategoriesOutputDTO.Item> items = categories.stream()
                .map(c -> new ListCategoriesOutputDTO.Item(
                        c.getId(),
                        c.getName().value(),
                        c.getColor().value(),
                        c.isArchived()
                ))
                .toList();

        return new ListCategoriesOutputDTO(items);
    }
}
