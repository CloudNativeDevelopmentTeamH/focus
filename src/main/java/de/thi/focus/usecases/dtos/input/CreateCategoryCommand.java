package de.thi.focus.usecases.dtos.input;

import de.thi.focus.entities.ids.UserId;

public record CreateCategoryCommand(
        UserId userId,
        String name,
        String color // nullable
) {}
