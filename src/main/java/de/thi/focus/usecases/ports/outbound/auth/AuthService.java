package de.thi.focus.usecases.ports.outbound.auth;

import de.thi.focus.entities.ids.UserId;
import io.smallrye.mutiny.Uni;

public interface AuthService {
    Uni<UserId> authenticate(String token);
}
