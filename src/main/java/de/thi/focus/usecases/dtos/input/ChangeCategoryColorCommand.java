package de.thi.focus.usecases.dtos.input;

import de.thi.focus.entities.ids.CategoryId;
import de.thi.focus.entities.ids.UserId;

public record ChangeCategoryColorCommand(
        UserId userId,
        CategoryId categoryId,
        String color
) {}
