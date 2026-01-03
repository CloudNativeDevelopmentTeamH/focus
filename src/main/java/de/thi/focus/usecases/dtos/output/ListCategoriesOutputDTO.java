package de.thi.focus.usecases.dtos.output;

import de.thi.focus.entities.ids.CategoryId;

import java.util.List;

public record ListCategoriesOutputDTO(List<Item> categories) {

    public record Item(
            CategoryId categoryId,
            String name,
            String color,
            boolean archived
    ) {}
}
