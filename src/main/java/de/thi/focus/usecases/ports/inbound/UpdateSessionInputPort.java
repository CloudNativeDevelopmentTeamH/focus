package de.thi.focus.usecases.ports.inbound;

import de.thi.focus.usecases.dtos.input.UpdateSessionCommand;

public interface UpdateSessionInputPort {
    void execute(UpdateSessionCommand command);
}
