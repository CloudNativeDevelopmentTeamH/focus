package de.thi.focus.usecases.ports.inbound;

import de.thi.focus.usecases.dtos.input.StopSessionCommand;
import de.thi.focus.usecases.dtos.output.StopSessionOutputDTO;

public interface StopSessionInputPort {

    StopSessionOutputDTO execute(StopSessionCommand command);
}
