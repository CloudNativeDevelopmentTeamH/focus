package de.thi.focus.interfaceadapters.health;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/readyz")
@Produces(MediaType.APPLICATION_JSON)
public class ReadinessResource {

    @GET
    public Response readinessCheck() {
        // TODO: Implement actual readiness checks
        return Response.ok().entity("{\"status\": \"UP\"}").build();
    }
}
