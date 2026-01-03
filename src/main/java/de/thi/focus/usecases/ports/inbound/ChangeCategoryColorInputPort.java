package de.thi.focus.usecases.ports.inbound;

import de.thi.focus.usecases.dtos.input.ChangeCategoryColorCommand;
import de.thi.focus.usecases.dtos.output.ChangeCategoryColorOutputDTO;

public interface ChangeCategoryColorInputPort {
    ChangeCategoryColorOutputDTO execute(ChangeCategoryColorCommand command);
}
