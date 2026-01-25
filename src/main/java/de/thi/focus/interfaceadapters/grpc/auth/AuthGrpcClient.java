package de.thi.focus.interfaceadapters.grpc.auth;

import de.thi.focus.auth.grpc.AuthenticateRequest;
import de.thi.focus.auth.grpc.MutinyAuthServiceGrpc;
import de.thi.focus.entities.ids.UserId;
import de.thi.focus.usecases.ports.outbound.auth.AuthService;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AuthGrpcClient implements AuthService {

    @GrpcClient("auth")
    MutinyAuthServiceGrpc.MutinyAuthServiceStub auth;

    @Override
    public Uni<UserId> authenticate(String token) {
        AuthenticateRequest req = AuthenticateRequest.newBuilder()
                .setToken(token)
                .build();

        return auth.authenticate(req)
                .map(resp -> new UserId(resp.getUserId()));
    }
}
