package de.thi.focus.interfaceadapters.web;

import de.thi.focus.entities.ids.CategoryId;
import de.thi.focus.entities.ids.FocusSessionId;
import de.thi.focus.entities.ids.UserId;

import de.thi.focus.interfaceadapters.web.dto.ResumeSessionHttpRequest;
import de.thi.focus.interfaceadapters.web.dto.StartSessionHttpRequest;
import de.thi.focus.interfaceadapters.web.dto.StopSessionHttpRequest;
import de.thi.focus.interfaceadapters.web.presenter.SessionHttpPresenter;

import de.thi.focus.usecases.dtos.input.ResumeSessionCommand;
import de.thi.focus.usecases.dtos.input.StartSessionCommand;
import de.thi.focus.usecases.dtos.input.StopSessionCommand;

import de.thi.focus.usecases.dtos.output.GetRunningSessionOutputDTO;
import de.thi.focus.usecases.dtos.output.ResumeSessionOutputDTO;
import de.thi.focus.usecases.dtos.output.StartSessionOutputDTO;
import de.thi.focus.usecases.dtos.output.StopSessionOutputDTO;

import de.thi.focus.usecases.ports.inbound.GetRunningSessionInputPort;
import de.thi.focus.usecases.ports.inbound.ResumeSessionInputPort;
import de.thi.focus.usecases.ports.inbound.StartSessionInputPort;
import de.thi.focus.usecases.ports.inbound.StopSessionInputPort;

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

    public SessionController(
            StartSessionInputPort startSession,
            StopSessionInputPort stopSession,
            ResumeSessionInputPort resumeSession,
            SessionHttpPresenter presenter,
            GetRunningSessionInputPort getRunningSession
    ) {
        this.startSession = Objects.requireNonNull(startSession);
        this.stopSession = Objects.requireNonNull(stopSession);
        this.resumeSession = Objects.requireNonNull(resumeSession);
        this.presenter = Objects.requireNonNull(presenter);
        this.getRunningSession = Objects.requireNonNull(getRunningSession);
    }

    @POST
    @Path("/start")
    public Response start(
            @HeaderParam("X-User-Id") String userIdHeader,
            StartSessionHttpRequest request
    ) {
        UserId userId = UserId.fromString(requireHeader(userIdHeader, "X-User-Id"));

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
            @HeaderParam("X-User-Id") String userIdHeader,
            @QueryParam("sessionId") String sessionId,
            StopSessionHttpRequest request
    ) {
        UserId userId = UserId.fromString(requireHeader(userIdHeader, "X-User-Id"));

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
            @HeaderParam("X-User-Id") String userIdHeader,
            @QueryParam("previousSessionId") String previousSessionId,
            ResumeSessionHttpRequest request
    ) {
        UserId userId = UserId.fromString(requireHeader(userIdHeader, "X-User-Id"));

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
    public Response running(@HeaderParam("X-User-Id") String userIdHeader) {
        UserId userId = UserId.fromString(requireHeader(userIdHeader, "X-User-Id"));

        GetRunningSessionOutputDTO output = getRunningSession.execute(userId);
        return presenter.present(output);
    }

    private static String requireHeader(String value, String headerName) {
        if (value == null || value.isBlank()) {
            // mapped by GlobalExceptionMapper to 400 INVALID_REQUEST
            throw new IllegalArgumentException(headerName + " header is required");
        }
        return value;
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
