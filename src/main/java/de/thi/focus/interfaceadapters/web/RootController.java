package de.thi.focus.interfaceadapters.web;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public final class RootController {

    @GET
    public Response root() {
        return Response.ok(Map.of(
                "service", "Focus API",
                "version", "1.0",
                "status", "running",
                "endpoints", Map.of(
                        "sessions", "/sessions",
                        "categories", "/categories",
                        "health", "/healthz",
                        "readiness", "/readyz",
                )
        )).build();
    }
}
