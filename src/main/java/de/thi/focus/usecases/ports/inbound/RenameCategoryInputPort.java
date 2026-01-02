package de.thi.focus.usecases.ports.inbound;

import de.thi.focus.usecases.dtos.input.RenameCategoryCommand;
import de.thi.focus.usecases.dtos.output.RenameCategoryOutputDTO;

public interface RenameCategoryInputPort {

    RenameCategoryOutputDTO execute(RenameCategoryCommand command);
}
