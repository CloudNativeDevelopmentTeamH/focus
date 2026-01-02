package de.thi.focus.usecases.dtos.input;

import de.thi.focus.entities.ids.CategoryId;

public record CategoryView(
        CategoryId id,
        String name,
        String color,
        boolean archived
) {}
