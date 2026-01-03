package de.thi.focus.interfaceadapters.web.exception;

import de.thi.focus.entities.errors.*;
import de.thi.focus.interfaceadapters.web.dto.ErrorResponse;
import de.thi.focus.usecases.errors.*;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.WebApplicationException;

@Provider
public final class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable ex) {

        // -------------------------
        // Framework / routing errors
        // -------------------------
        if (ex instanceof NotFoundException) {
            return json(Response.Status.NOT_FOUND, "ROUTE_NOT_FOUND", ex.getMessage());
        }

        if (ex instanceof BadRequestException) {
            return json(Response.Status.BAD_REQUEST, "INVALID_REQUEST", ex.getMessage());
        }

        // -------------------------
        // Use case errors
        // -------------------------
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

        if (ex instanceof CategoryInUseException) {
            return json(Response.Status.CONFLICT, "FAILED_PRECONDITION", ex.getMessage());
        }

        // -------------------------
        // Domain errors
        // -------------------------
        if (ex instanceof DomainException) {
            // Keep it generic: domain failures are usually client-caused or precondition failures
            return json(Response.Status.BAD_REQUEST, "DOMAIN_ERROR", ex.getMessage());
        }

        // -------------------------
        // Parsing / mapping issues
        // -------------------------
        if (ex instanceof java.time.format.DateTimeParseException || ex instanceof IllegalArgumentException) {
            return json(Response.Status.BAD_REQUEST, "INVALID_REQUEST", ex.getMessage());
        }

        if (ex instanceof WebApplicationException wae) {
            int status = wae.getResponse() != null ? wae.getResponse().getStatus() : 500;

            Response.Status httpStatus = Response.Status.fromStatusCode(status);
            if (httpStatus == null) {
                httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
            }

            String code = (status >= 400 && status < 500) ? "INVALID_REQUEST" : "INTERNAL";
            String msg = wae.getMessage() != null ? wae.getMessage() : httpStatus.getReasonPhrase();

            return json(httpStatus, code, msg);
        }

        // -------------------------
        // Fallback (INTERNAL)
        // -------------------------
        // For initial debugging, include the exception class to avoid blind "Unexpected error"
        String msg = ex.getMessage();
        String details = (msg == null || msg.isBlank())
                ? ex.getClass().getName()
                : (ex.getClass().getName() + ": " + msg);

        return json(Response.Status.INTERNAL_SERVER_ERROR, "INTERNAL", details);
    }

    private static Response json(Response.Status status, String code, String message) {
        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorResponse(code, message))
                .build();
    }
}
