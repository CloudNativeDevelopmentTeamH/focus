package de.thi.focus.usecases.dtos.output;

public record GetRunningSessionOutputDTO(
        boolean running,
        String sessionId,
        String startedAt,
        String categoryId,
        String note
) {}
