package de.thi.focus.usecases.ports.inbound;

import de.thi.focus.usecases.dtos.input.CreateCategoryCommand;
import de.thi.focus.usecases.dtos.output.CreateCategoryOutputDTO;

public interface CreateCategoryInputPort {
    CreateCategoryOutputDTO execute(CreateCategoryCommand command);
}
