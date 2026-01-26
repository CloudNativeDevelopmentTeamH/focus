package de.thi.focus.interfaceadapters.web;

import de.thi.focus.entities.ids.UserId;
import de.thi.focus.frameworksdrivers.persistence.JpaAuthSessionRepository;
import de.thi.focus.frameworksdrivers.persistence.jpa.AuthSessionEntity;
import de.thi.focus.usecases.ports.outbound.auth.AuthService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthController {

    private static final String COOKIE_SID = "focus_sid";
    private static final String COOKIE_CSRF = "focus_csrf";
    private static final Duration SESSION_TTL = Duration.ofHours(12);

    private final AuthService authService;
    private final JpaAuthSessionRepository sessions;
    private final boolean secureCookies;

    public AuthController(
            AuthService authService,
            JpaAuthSessionRepository sessions,
            @org.eclipse.microprofile.config.inject.ConfigProperty(name = "focus.security.secure-cookies")
            boolean secureCookies
    ) {
        this.authService = authService;
        this.sessions = sessions;
        this.secureCookies = secureCookies;
    }

    @POST
    @Path("/session")
    public Response login(@HeaderParam("Authorization") String authorization) {
        String token = extractBearerToken(requireHeader(authorization, "Authorization"));

        // Auth gRPC: Authenticate(token) -> user_id
        UserId userId = authService.authenticate(token).await().indefinitely();

        Instant now = Instant.now();
        UUID sid = UUID.randomUUID();

        AuthSessionEntity s = new AuthSessionEntity();
        s.id = sid;
        s.userId = userId.value();     // int
        s.token = token;
        s.createdAt = now;
        s.lastSeenAt = now;
        s.expiresAt = now.plus(SESSION_TTL);

        sessions.persist(s);

        NewCookie sidCookie = new NewCookie.Builder(COOKIE_SID)
                .value(sid.toString())
                .path("/")
                .httpOnly(true)
                .secure(isProdSecure())
                .sameSite(NewCookie.SameSite.LAX)
                .maxAge((int) SESSION_TTL.toSeconds())
                .build();

        // CSRF double-submit token
        String csrf = UUID.randomUUID().toString();
        NewCookie csrfCookie = new NewCookie.Builder(COOKIE_CSRF)
                .value(csrf)
                .path("/")
                .httpOnly(false)
                .secure(isProdSecure())
                .sameSite(NewCookie.SameSite.LAX)
                .maxAge((int) SESSION_TTL.toSeconds())
                .build();

        return Response.noContent()
                .cookie(sidCookie, csrfCookie)
                .build();
    }

    @POST
    @Path("/logout")
    public Response logout(@CookieParam(COOKIE_SID) String sidRaw) {
        if (sidRaw != null && !sidRaw.isBlank()) {
            try {
                UUID sid = UUID.fromString(sidRaw.trim());
                sessions.revoke(sid, Instant.now());
            } catch (IllegalArgumentException ignored) {
                // treat as already logged out; still clear cookies
            }
        }

        NewCookie clearSid = new NewCookie.Builder(COOKIE_SID)
                .value("")
                .path("/")
                .httpOnly(true)
                .secure(isProdSecure())
                .sameSite(NewCookie.SameSite.LAX)
                .maxAge(0)
                .build();

        NewCookie clearCsrf = new NewCookie.Builder(COOKIE_CSRF)
                .value("")
                .path("/")
                .httpOnly(false)
                .secure(isProdSecure())
                .sameSite(NewCookie.SameSite.LAX)
                .maxAge(0)
                .build();

        return Response.noContent()
                .cookie(clearSid, clearCsrf)
                .build();
    }

    private static String requireHeader(String value, String headerName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(headerName + " header is required");
        }
        return value;
    }

    private static String extractBearerToken(String authorization) {
        String trimmed = authorization.trim();
        if (!trimmed.regionMatches(true, 0, "Bearer ", 0, 7)) {
            throw new IllegalArgumentException("Authorization must be a Bearer token");
        }
        String token = trimmed.substring(7).trim();
        if (token.isEmpty()) throw new IllegalArgumentException("Bearer token must not be blank");
        return token;
    }

    private boolean isProdSecure() {
        return secureCookies;
    }
}
