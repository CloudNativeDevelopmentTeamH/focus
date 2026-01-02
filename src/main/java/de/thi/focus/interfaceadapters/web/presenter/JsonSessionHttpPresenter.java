package de.thi.focus.interfaceadapters.web.presenter;

import de.thi.focus.usecases.dtos.output.ResumeSessionOutputDTO;
import de.thi.focus.usecases.dtos.output.StartSessionOutputDTO;
import de.thi.focus.usecases.dtos.output.StopSessionOutputDTO;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

public final class JsonSessionHttpPresenter implements SessionHttpPresenter {

    @Override
    public Response present(StartSessionOutputDTO output) {
        return Response.status(Response.Status.CREATED)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of("sessionId", output.sessionId().toString()))
                .build();
    }

    @Override
    public Response present(ResumeSessionOutputDTO output) {
        return Response.status(Response.Status.CREATED)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of("sessionId", output.sessionId().toString()))
                .build();
    }

    @Override
    public Response present(StopSessionOutputDTO output) {
        return Response.noContent().build(); // 204
    }
}
