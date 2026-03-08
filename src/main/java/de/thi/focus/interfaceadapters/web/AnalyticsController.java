package de.thi.focus.interfaceadapters.web;

import de.thi.focus.interfaceadapters.web.security.CurrentUser;
import de.thi.focus.usecases.ports.outbound.analytics.AnalyticsService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Objects;

@Path("/analytics")
@Produces(MediaType.APPLICATION_JSON)
public final class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final CurrentUser currentUser;

    public AnalyticsController(AnalyticsService analyticsService, CurrentUser currentUser) {
        this.analyticsService = Objects.requireNonNull(analyticsService);
        this.currentUser = Objects.requireNonNull(currentUser);
    }

    @GET
    @Path("/general")
    public Response general() {
        int userId = currentUser.userId().value();
        AnalyticsService.GeneralStats response = analyticsService.getGeneralStats(userId)
                .await().indefinitely();

        return Response.ok(response).build();
    }

    @GET
    @Path("/category")
    public Response category(@QueryParam("categoryId") String categoryId) {
        if (categoryId == null || categoryId.isBlank()) {
            throw new IllegalArgumentException("categoryId query parameter is required");
        }

        int userId = currentUser.userId().value();
        AnalyticsService.AverageByCategory response = analyticsService
                .getAverageLengthByCategory(userId, categoryId)
                .await().indefinitely();

        return Response.ok(response).build();
    }
}
