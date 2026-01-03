package de.thi.focus.interfaceadapters.web.presenter;

import de.thi.focus.usecases.dtos.output.*;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

public final class JsonCategoryHttpPresenter implements CategoryHttpPresenter {

    @Override
    public Response present(CreateCategoryOutputDTO output) {
        return Response.status(Response.Status.CREATED)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of("categoryId", output.categoryId().toString()))
                .build();
    }

    @Override
    public Response present(RenameCategoryOutputDTO output) {
        return Response.ok()
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of("categoryId", output.categoryId().toString()))
                .build();
    }

    @Override
    public Response present(ArchiveCategoryOutputDTO output) {
        return Response.noContent().build();
    }

    @Override
    public Response present(ListCategoriesOutputDTO output) {
        return Response.ok()
                .type(MediaType.APPLICATION_JSON)
                .entity(output)
                .build();
    }

    @Override
    public Response present(ChangeCategoryColorOutputDTO output) {
        return Response.noContent().build();
    }

    @Override
    public Response present(UnarchiveCategoryOutputDTO output) {
        return Response.noContent().build();
    }
}
