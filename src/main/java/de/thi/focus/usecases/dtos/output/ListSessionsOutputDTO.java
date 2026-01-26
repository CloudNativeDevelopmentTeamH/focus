package de.thi.focus.usecases.dtos.output;

import java.util.List;

public record ListSessionsOutputDTO(List<Item> sessions) {
    
    public record Item(
            String sessionId,
            String startedAt,
            String endedAt,
            String categoryId,
            String note
    ) {}
}
