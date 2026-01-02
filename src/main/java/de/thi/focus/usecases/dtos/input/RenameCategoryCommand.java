package de.thi.focus.usecases.dtos.input;

import de.thi.focus.entities.ids.CategoryId;
import de.thi.focus.entities.ids.UserId;

public record RenameCategoryCommand(
        UserId userId,
        CategoryId categoryId,
        String newName
) {}
