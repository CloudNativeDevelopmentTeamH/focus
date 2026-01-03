package de.thi.focus.interfaceadapters.web;

import de.thi.focus.entities.ids.CategoryId;
import de.thi.focus.entities.ids.UserId;
import de.thi.focus.interfaceadapters.web.dto.CreateCategoryHttpRequest;
import de.thi.focus.interfaceadapters.web.dto.RenameCategoryHttpRequest;
import de.thi.focus.interfaceadapters.web.presenter.CategoryHttpPresenter;
import de.thi.focus.usecases.dtos.input.ArchiveCategoryCommand;
import de.thi.focus.usecases.dtos.input.CreateCategoryCommand;
import de.thi.focus.usecases.dtos.input.RenameCategoryCommand;
import de.thi.focus.usecases.dtos.output.ArchiveCategoryOutputDTO;
import de.thi.focus.usecases.dtos.output.CreateCategoryOutputDTO;
import de.thi.focus.usecases.dtos.output.ListCategoriesOutputDTO;
import de.thi.focus.usecases.dtos.output.RenameCategoryOutputDTO;
import de.thi.focus.usecases.ports.inbound.ArchiveCategoryInputPort;
import de.thi.focus.usecases.ports.inbound.CreateCategoryInputPort;
import de.thi.focus.usecases.ports.inbound.ListCategoriesInputPort;
import de.thi.focus.usecases.ports.inbound.RenameCategoryInputPort;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Objects;

@Path("/categories")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public final class CategoryController {

    private final CreateCategoryInputPort createCategory;
    private final RenameCategoryInputPort renameCategory;
    private final ArchiveCategoryInputPort archiveCategory;
    private final ListCategoriesInputPort listCategories;
    private final CategoryHttpPresenter presenter;

    public CategoryController(
            CreateCategoryInputPort createCategory,
            RenameCategoryInputPort renameCategory,
            ArchiveCategoryInputPort archiveCategory,
            ListCategoriesInputPort listCategories,
            CategoryHttpPresenter presenter
    ) {
        this.createCategory = Objects.requireNonNull(createCategory);
        this.renameCategory = Objects.requireNonNull(renameCategory);
        this.archiveCategory = Objects.requireNonNull(archiveCategory);
        this.listCategories = Objects.requireNonNull(listCategories);
        this.presenter = Objects.requireNonNull(presenter);
    }

    @POST
    @Path("/create")
    public Response create(@HeaderParam("X-User-Id") String userIdHeader, CreateCategoryHttpRequest request) {
        UserId userId = UserId.fromString(requireHeader(userIdHeader, "X-User-Id"));

        if (request == null) request = new CreateCategoryHttpRequest();

        CreateCategoryOutputDTO output = createCategory.execute(
                new CreateCategoryCommand(userId, request.name, request.color)
        );

        return presenter.present(output);
    }

    @POST
    @Path("/rename")
    public Response rename(
            @HeaderParam("X-User-Id") String userIdHeader,
            @QueryParam("categoryId") String categoryId,
            RenameCategoryHttpRequest request
    ) {
        UserId userId = UserId.fromString(requireHeader(userIdHeader, "X-User-Id"));

        if (categoryId == null || categoryId.isBlank()) {
            throw new IllegalArgumentException("categoryId query parameter is required");
        }
        if (request == null) request = new RenameCategoryHttpRequest();

        RenameCategoryOutputDTO output = renameCategory.execute(
                new RenameCategoryCommand(userId, CategoryId.fromString(categoryId), request.newName)
        );

        return presenter.present(output);
    }

    @POST
    @Path("/archive")
    public Response archive(
            @HeaderParam("X-User-Id") String userIdHeader,
            @QueryParam("categoryId") String categoryId
    ) {
        UserId userId = UserId.fromString(requireHeader(userIdHeader, "X-User-Id"));

        if (categoryId == null || categoryId.isBlank()) {
            throw new IllegalArgumentException("categoryId query parameter is required");
        }

        ArchiveCategoryOutputDTO output = archiveCategory.execute(
                new ArchiveCategoryCommand(userId, CategoryId.fromString(categoryId))
        );

        return presenter.present(output);
    }

    @GET
    public Response list(@HeaderParam("X-User-Id") String userIdHeader) {
        UserId userId = UserId.fromString(requireHeader(userIdHeader, "X-User-Id"));

        ListCategoriesOutputDTO output = listCategories.execute(userId);
        return presenter.present(output);
    }

    private static String requireHeader(String value, String headerName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(headerName + " header is required");
        }
        return value;
    }
}
