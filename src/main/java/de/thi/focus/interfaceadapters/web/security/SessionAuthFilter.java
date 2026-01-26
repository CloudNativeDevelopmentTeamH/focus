package de.thi.focus.interfaceadapters.web.security;

import de.thi.focus.entities.ids.UserId;
import de.thi.focus.frameworksdrivers.persistence.JpaAuthSessionRepository;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.time.Instant;
import java.util.UUID;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class SessionAuthFilter implements ContainerRequestFilter {

    private static final String COOKIE_SID = "focus_sid";
    private static final String COOKIE_CSRF = "focus_csrf";
    private static final String HEADER_CSRF = "X-CSRF-Token";

    private final JpaAuthSessionRepository sessions;
    private final CurrentUser currentUser;

    public SessionAuthFilter(JpaAuthSessionRepository sessions, CurrentUser currentUser) {
        this.sessions = sessions;
        this.currentUser = currentUser;
    }

    @Override
    public void filter(ContainerRequestContext ctx) {
        String path = ctx.getUriInfo().getPath();

        // Allow unauthenticated endpoints
        if (path.equals("auth") || path.startsWith("auth/") || 
            path.equals("/auth") || path.startsWith("/auth/")) return;

        // Health endpoints
        if (path.equals("healthz") || path.startsWith("healthz/") ||
            path.equals("/healthz") || path.startsWith("/healthz/")) return;
        if (path.equals("readyz") || path.startsWith("readyz/") ||
            path.equals("/readyz") || path.startsWith("/readyz/")) return;

        // CORS Preflight
        if ("OPTIONS".equals(ctx.getMethod())) return;

        Cookie sidCookie = ctx.getCookies().get(COOKIE_SID);
        if (sidCookie == null || sidCookie.getValue() == null || sidCookie.getValue().isBlank()) {
            abort401(ctx);
            return;
        }

        UUID sid;
        try {
            sid = UUID.fromString(sidCookie.getValue().trim());
        } catch (IllegalArgumentException e) {
            abort401(ctx);
            return;
        }

        if (isStateChanging(ctx) && !csrfValid(ctx)) {
            ctx.abortWith(Response.status(403).entity("CSRF validation failed").build());
            return;
        }

        var now = Instant.now();
        var sessionOpt = sessions.findActive(sid, now);
        if (sessionOpt.isEmpty()) {
            abort401(ctx);
            return;
        }

        var session = sessionOpt.get();

        currentUser.setUserId(new UserId(session.userId));
    }

    private static boolean isStateChanging(ContainerRequestContext ctx) {
        String m = ctx.getMethod();
        return "POST".equals(m) || "PUT".equals(m) || "DELETE".equals(m) || "PATCH".equals(m);
    }

    private static boolean csrfValid(ContainerRequestContext ctx) {
        Cookie csrfCookie = ctx.getCookies().get(COOKIE_CSRF);
        String header = ctx.getHeaderString(HEADER_CSRF);

        if (csrfCookie == null || csrfCookie.getValue() == null) return false;
        if (header == null) return false;

        return csrfCookie.getValue().equals(header);
    }

    private static void abort401(ContainerRequestContext ctx) {
        ctx.abortWith(Response.status(401).build());
    }
}
