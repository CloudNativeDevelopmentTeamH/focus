package de.thi.focus.usecases.ports.inbound;

import de.thi.focus.usecases.dtos.input.StartSessionCommand;
import de.thi.focus.usecases.dtos.output.StartSessionOutputDTO;

public interface StartSessionInputPort {

    StartSessionOutputDTO execute(StartSessionCommand command);
}
