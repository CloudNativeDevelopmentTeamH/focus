package de.thi.focus.usecases.dtos.input;

import de.thi.focus.entities.ids.CategoryId;
import de.thi.focus.entities.ids.UserId;

public record RecolorCategoryCommand(
        UserId userId,
        CategoryId categoryId,
        String newColor
) {}
