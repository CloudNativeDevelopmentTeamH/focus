package de.thi.focus.interfaceadapters.web.exception;

import de.thi.focus.entities.errors.*;
import de.thi.focus.interfaceadapters.web.dto.ErrorResponse;
import de.thi.focus.usecases.errors.*;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Maps domain + use case exceptions to HTTP responses.
 * Adapter layer concern only.
 */
@Provider
public final class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception ex) {

        // ---------- Use case errors ----------
        if (ex instanceof SessionNotFoundException || ex instanceof CategoryNotFoundException) {
            return json(Response.Status.NOT_FOUND, "NOT_FOUND", ex.getMessage());
        }

        if (ex instanceof SessionAccessDeniedException || ex instanceof CategoryAccessDeniedException) {
            return json(Response.Status.FORBIDDEN, "FORBIDDEN", ex.getMessage());
        }

        if (ex instanceof RunningSessionAlreadyExistsException || ex instanceof CategoryNameAlreadyExistsException) {
            return json(Response.Status.CONFLICT, "CONFLICT", ex.getMessage());
        }

        if (ex instanceof NoPreviousSessionToResumeException) {
            return json(Response.Status.CONFLICT, "NO_PREVIOUS_SESSION", ex.getMessage());
        }

        // ---------- Domain errors ----------
        // invalid input / invariants violated
        if (ex instanceof InvalidTimeRangeException
                || ex instanceof InvalidColorException
                || ex instanceof InvalidCategoryNameException
                || ex instanceof NoteTooLongException) {
            return json(Response.Status.BAD_REQUEST, "INVALID_ARGUMENT", ex.getMessage());
        }

        // state-related conflicts
        if (ex instanceof SessionAlreadyStoppedException || ex instanceof SessionStillRunningException) {
            return json(Response.Status.CONFLICT, "FAILED_PRECONDITION", ex.getMessage());
        }

        // ---------- Parsing / mapping issues ----------
        if (ex instanceof java.time.format.DateTimeParseException
                || ex instanceof IllegalArgumentException) {
            // UUID.fromString throws IllegalArgumentException, Instant.parse throws DateTimeParseException
            return json(Response.Status.BAD_REQUEST, "INVALID_REQUEST", ex.getMessage());
        }

        // ---------- Fallback ----------
        return json(Response.Status.INTERNAL_SERVER_ERROR, "INTERNAL", "Unexpected error");
    }

    private static Response json(Response.Status status, String code, String message) {
        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorResponse(code, message))
                .build();
    }
}
