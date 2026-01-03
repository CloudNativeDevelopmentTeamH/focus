package de.thi.focus.usecases.ports.inbound;

import de.thi.focus.usecases.dtos.input.ArchiveCategoryCommand;
import de.thi.focus.usecases.dtos.output.ArchiveCategoryOutputDTO;

public interface ArchiveCategoryInputPort {
    ArchiveCategoryOutputDTO execute(ArchiveCategoryCommand command);
}
