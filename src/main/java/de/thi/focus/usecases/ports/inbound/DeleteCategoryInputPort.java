package de.thi.focus.usecases.ports.inbound;

import de.thi.focus.usecases.dtos.input.DeleteCategoryCommand;
import de.thi.focus.usecases.dtos.output.DeleteCategoryOutputDTO;

public interface DeleteCategoryInputPort {
    DeleteCategoryOutputDTO execute(DeleteCategoryCommand command);
}
