package de.thi.focus.usecases.ports.inbound;

import de.thi.focus.entities.ids.UserId;
import de.thi.focus.usecases.dtos.output.ListSessionsOutputDTO;

public interface ListSessionsInputPort {
    ListSessionsOutputDTO execute(UserId userId);
}
