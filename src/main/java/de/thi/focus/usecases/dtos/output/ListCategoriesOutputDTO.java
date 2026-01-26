package de.thi.focus.usecases.dtos.output;

import java.util.List;

public record ListCategoriesOutputDTO(List<Item> categories) {

    public record Item(
            String categoryId,
            String name,
            String color,
            boolean archived
    ) {}
}
