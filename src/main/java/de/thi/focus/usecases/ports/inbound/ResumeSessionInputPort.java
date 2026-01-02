package de.thi.focus.usecases.ports.inbound;

import de.thi.focus.usecases.dtos.input.ResumeSessionCommand;
import de.thi.focus.usecases.dtos.output.ResumeSessionOutputDTO;

public interface ResumeSessionInputPort {

    ResumeSessionOutputDTO execute(ResumeSessionCommand command);
}
