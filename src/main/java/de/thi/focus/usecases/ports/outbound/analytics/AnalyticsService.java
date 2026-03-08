package de.thi.focus.usecases.ports.outbound.analytics;

import io.smallrye.mutiny.Uni;

import java.util.List;

public interface AnalyticsService {

    Uni<GeneralStats> getGeneralStats(int userId);

    Uni<AverageByCategory> getAverageLengthByCategory(int userId, String categoryId);

    record GeneralStats(
            double averageLengthOverallSeconds,
            double averageLengthLast10Seconds,
            List<CategoryShare> categoryShares
    ) {
        public record CategoryShare(
                String categoryId,
                double share,
                long totalSeconds
        ) {
        }
    }

    record AverageByCategory(
            String categoryId,
            double averageLengthSeconds,
            long count,
            long sumSeconds
    ) {
    }
}
