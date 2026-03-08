package de.thi.focus.interfaceadapters.grpc.analytics;

import de.thi.focus.analytics.grpc.GetAverageLengthByCategoryRequest;
import de.thi.focus.analytics.grpc.GetGeneralStatsRequest;
import de.thi.focus.analytics.grpc.MutinyAnalyticsServiceGrpc;
import de.thi.focus.usecases.ports.outbound.analytics.AnalyticsService;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.stream.Collectors;

@ApplicationScoped
public class AnalyticsGrpcClient implements AnalyticsService {

    @GrpcClient("analytics")
    MutinyAnalyticsServiceGrpc.MutinyAnalyticsServiceStub analytics;

    @Override
    public Uni<GeneralStats> getGeneralStats(int userId) {
        GetGeneralStatsRequest request = GetGeneralStatsRequest.newBuilder()
                .setUserId(String.valueOf(userId))
                .build();

        return analytics.getGeneralStats(request)
                .onFailure(StatusRuntimeException.class).transform(AnalyticsGrpcClient::mapGrpcError)
                .map(resp -> new GeneralStats(
                        resp.getAverageLengthOverallSeconds(),
                        resp.getAverageLengthLast10Seconds(),
                        resp.getCategorySharesList().stream()
                                .map(cs -> new GeneralStats.CategoryShare(
                                        cs.getCategoryId(),
                                        cs.getShare(),
                                        cs.getTotalSeconds()
                                ))
                                .collect(Collectors.toList())
                ));
    }

    @Override
    public Uni<AverageByCategory> getAverageLengthByCategory(int userId, String categoryId) {
        GetAverageLengthByCategoryRequest request = GetAverageLengthByCategoryRequest.newBuilder()
                .setUserId(String.valueOf(userId))
                .setCategoryId(categoryId)
                .build();

        return analytics.getAverageLengthByCategory(request)
                .onFailure(StatusRuntimeException.class).transform(AnalyticsGrpcClient::mapGrpcError)
                .map(resp -> new AverageByCategory(
                        resp.getCategoryId(),
                        resp.getAverageLengthSeconds(),
                        resp.getCount(),
                        resp.getSumSeconds()
                ));
    }

    private static RuntimeException mapGrpcError(StatusRuntimeException e) {
        if (e.getStatus().getCode() == Status.Code.INVALID_ARGUMENT) {
            return new IllegalArgumentException(e.getStatus().getDescription());
        }
        return e;
    }
}
