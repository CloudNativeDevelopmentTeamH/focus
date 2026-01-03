package de.thi.focus.usecases.dtos.output;

import de.thi.focus.entities.ids.FocusSessionId;

public record ResumeSessionOutputDTO(
        FocusSessionId sessionId,
        FocusSessionId resumedFromSessionId
        ) {}
