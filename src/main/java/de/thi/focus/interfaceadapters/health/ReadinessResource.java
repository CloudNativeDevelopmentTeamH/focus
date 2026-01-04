package de.thi.focus.interfaceadapters.health;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/readyz")
@Produces(MediaType.APPLICATION_JSON)
public class ReadinessResource {

    @Inject
    EntityManager em;

    @GET
    public Response readinessCheck() {
        boolean dbReady = checkDatabaseConnection();

        if (dbReady) {
            return Response.ok()
                    .entity("{\"status\": \"UP\", \"checks\": {\"database\": \"UP\"}}")
                    .build();
        } else {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity("{\"status\": \"DOWN\", \"checks\": {\"database\": \"DOWN\"}}")
                    .build();
        }
    }

    private boolean checkDatabaseConnection() {
        try {
            em.createNativeQuery("SELECT 1").getSingleResult();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
