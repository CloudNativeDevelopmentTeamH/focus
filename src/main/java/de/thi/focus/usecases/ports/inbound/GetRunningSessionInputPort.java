package de.thi.focus.usecases.ports.inbound;

import de.thi.focus.entities.ids.UserId;
import de.thi.focus.usecases.dtos.output.GetRunningSessionOutputDTO;

public interface GetRunningSessionInputPort {
    GetRunningSessionOutputDTO execute(UserId userId);
}
