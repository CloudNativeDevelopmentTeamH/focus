package de.thi.focus.interfaceadapters.web;

import de.thi.focus.entities.ids.CategoryId;
import de.thi.focus.entities.ids.FocusSessionId;
import de.thi.focus.entities.ids.UserId;

import de.thi.focus.interfaceadapters.web.dto.ResumeSessionHttpRequest;
import de.thi.focus.interfaceadapters.web.dto.StartSessionHttpRequest;
import de.thi.focus.interfaceadapters.web.dto.StopSessionHttpRequest;
import de.thi.focus.interfaceadapters.web.dto.UpdateSessionHttpRequest;
import de.thi.focus.interfaceadapters.web.presenter.SessionHttpPresenter;

import de.thi.focus.interfaceadapters.web.security.CurrentUser;

import de.thi.focus.usecases.dtos.input.ResumeSessionCommand;
import de.thi.focus.usecases.dtos.input.StartSessionCommand;
import de.thi.focus.usecases.dtos.input.StopSessionCommand;

import de.thi.focus.usecases.dtos.input.UpdateSessionCommand;
import de.thi.focus.usecases.dtos.output.GetRunningSessionOutputDTO;
import de.thi.focus.usecases.dtos.output.ResumeSessionOutputDTO;
import de.thi.focus.usecases.dtos.output.StartSessionOutputDTO;
import de.thi.focus.usecases.dtos.output.StopSessionOutputDTO;

import de.thi.focus.usecases.ports.inbound.*;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.Instant;
import java.util.Objects;

@Path("/sessions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public final class SessionController {

    private final StartSessionInputPort startSession;
    private final StopSessionInputPort stopSession;
    private final ResumeSessionInputPort resumeSession;
    private final SessionHttpPresenter presenter;
    private final GetRunningSessionInputPort getRunningSession;
    private final UpdateSessionInputPort updateSession;
    private final CurrentUser currentUser;

    public SessionController(
            StartSessionInputPort startSession,
            StopSessionInputPort stopSession,
            ResumeSessionInputPort resumeSession,
            SessionHttpPresenter presenter,
            GetRunningSessionInputPort getRunningSession,
            UpdateSessionInputPort updateSession,
            CurrentUser currentUser
    ) {
        this.startSession = Objects.requireNonNull(startSession);
        this.stopSession = Objects.requireNonNull(stopSession);
        this.resumeSession = Objects.requireNonNull(resumeSession);
        this.presenter = Objects.requireNonNull(presenter);
        this.getRunningSession = Objects.requireNonNull(getRunningSession);
        this.updateSession = Objects.requireNonNull(updateSession);
        this.currentUser = Objects.requireNonNull(currentUser);
    }

    @POST
    @Path("/start")
    public Response start(StartSessionHttpRequest request) {
        UserId userId = currentUser.userId();

        Instant startedAt = parseInstantOrNull(request.startedAt);

        CategoryId categoryId = (request.categoryId == null || request.categoryId.isBlank())
                ? null
                : CategoryId.fromString(request.categoryId);

        StartSessionCommand command = new StartSessionCommand(
                userId,
                startedAt,
                categoryId,
                blankToNull(request.note)
        );

        StartSessionOutputDTO output = startSession.execute(command);
        return presenter.present(output);
    }

    @POST
    @Path("/stop")
    public Response stop(
            @QueryParam("sessionId") String sessionId,
            StopSessionHttpRequest request
    ) {
        UserId userId = currentUser.userId();

        if (sessionId == null || sessionId.isBlank()) {
            throw new IllegalArgumentException("sessionId query parameter is required");
        }

        StopSessionHttpRequest safe = (request != null) ? request : new StopSessionHttpRequest();

        StopSessionCommand command = new StopSessionCommand(
                userId,
                FocusSessionId.fromString(sessionId),
                parseInstantOrNull(safe.endedAt)
        );

        StopSessionOutputDTO output = stopSession.execute(command);
        return presenter.present(output);
    }

    @POST
    @Path("/resume")
    public Response resume(
            @QueryParam("previousSessionId") String previousSessionId,
            ResumeSessionHttpRequest request
    ) {
        UserId userId = currentUser.userId();

        if (previousSessionId == null || previousSessionId.isBlank()) {
            throw new IllegalArgumentException("previousSessionId query parameter is required");
        }

        ResumeSessionHttpRequest safe = (request != null) ? request : new ResumeSessionHttpRequest();

        ResumeSessionCommand command = new ResumeSessionCommand(
                userId,
                FocusSessionId.fromString(previousSessionId),
                parseInstantOrNull(safe.startedAt)
        );

        ResumeSessionOutputDTO output = resumeSession.execute(command);
        return presenter.present(output);
    }

    @GET
    @Path("/running")
    public Response running() {
        UserId userId = currentUser.userId();

        GetRunningSessionOutputDTO output = getRunningSession.execute(userId);
        return presenter.present(output);
    }

    @POST
    @Path("/update")
    public Response update(
            @QueryParam("sessionId") String sessionId,
            UpdateSessionHttpRequest request
    ) {
        UserId userId = currentUser.userId();

        if (sessionId == null || sessionId.isBlank()) {
            throw new IllegalArgumentException("sessionId query parameter is required");
        }

        if (request == null) request = new UpdateSessionHttpRequest();

        updateSession.execute(new UpdateSessionCommand(
                userId,
                FocusSessionId.fromString(sessionId),
                null,
                null,
                request.categoryId != null ? CategoryId.fromString(request.categoryId) : null,
                request.clearCategory,
                request.note,
                request.clearNote
        ));

        return Response.noContent().build();
    }

    private static Instant parseInstantOrNull(String raw) {
        if (raw == null || raw.isBlank()) return null;
        return Instant.parse(raw); // DateTimeParseException -> mapped to 400 by GlobalExceptionMapper
    }

    private static String blankToNull(String s) {
        if (s == null) return null;
        String trimmed = s.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
