package de.thi.focus.usecases.ports.inbound;

import de.thi.focus.usecases.dtos.input.UnarchiveCategoryCommand;
import de.thi.focus.usecases.dtos.output.UnarchiveCategoryOutputDTO;

public interface UnarchiveCategoryInputPort {
    UnarchiveCategoryOutputDTO execute(UnarchiveCategoryCommand command);
}
