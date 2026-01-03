package de.thi.focus.interfaceadapters.web;

import de.thi.focus.entities.ids.CategoryId;
import de.thi.focus.entities.ids.UserId;
import de.thi.focus.interfaceadapters.web.dto.ChangeCategoryColorHttpRequest;
import de.thi.focus.interfaceadapters.web.dto.CreateCategoryHttpRequest;
import de.thi.focus.interfaceadapters.web.dto.RenameCategoryHttpRequest;
import de.thi.focus.interfaceadapters.web.presenter.CategoryHttpPresenter;
import de.thi.focus.usecases.dtos.input.*;
import de.thi.focus.usecases.dtos.output.*;
import de.thi.focus.usecases.ports.inbound.*;

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
    private final DeleteCategoryInputPort deleteCategory;
    private final ListCategoriesInputPort listCategories;
    private final CategoryHttpPresenter presenter;
    private final ChangeCategoryColorInputPort changeCategoryColor;
    private final UnarchiveCategoryInputPort unarchiveCategory;

    public CategoryController(
            CreateCategoryInputPort createCategory,
            RenameCategoryInputPort renameCategory,
            ArchiveCategoryInputPort archiveCategory,
            DeleteCategoryInputPort deleteCategory,
            ListCategoriesInputPort listCategories,
            CategoryHttpPresenter presenter,
            ChangeCategoryColorInputPort changeCategoryColor,
            UnarchiveCategoryInputPort unarchiveCategory
    ) {
        this.createCategory = Objects.requireNonNull(createCategory);
        this.renameCategory = Objects.requireNonNull(renameCategory);
        this.archiveCategory = Objects.requireNonNull(archiveCategory);
        this.deleteCategory = Objects.requireNonNull(deleteCategory);
        this.listCategories = Objects.requireNonNull(listCategories);
        this.presenter = Objects.requireNonNull(presenter);
        this.changeCategoryColor = Objects.requireNonNull(changeCategoryColor);
        this.unarchiveCategory = Objects.requireNonNull(unarchiveCategory);
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

    @POST
    @Path("/unarchive")
    public Response unarchive(
            @HeaderParam("X-User-Id") String userIdHeader,
            @QueryParam("categoryId") String categoryId
    ) {
        UserId userId = UserId.fromString(requireHeader(userIdHeader, "X-User-Id"));

        if (categoryId == null || categoryId.isBlank()) {
            throw new IllegalArgumentException("categoryId query parameter is required");
        }

        UnarchiveCategoryOutputDTO output = unarchiveCategory.execute(
                new UnarchiveCategoryCommand(userId, CategoryId.fromString(categoryId))
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

    @POST
    @Path("/delete")
    public Response delete(
            @HeaderParam("X-User-Id") String userIdHeader,
            @QueryParam("categoryId") String categoryId
    ) {
        UserId userId = UserId.fromString(requireHeader(userIdHeader, "X-User-Id"));

        if (categoryId == null || categoryId.isBlank()) {
            throw new IllegalArgumentException("categoryId query parameter is required");
        }

        DeleteCategoryOutputDTO output = deleteCategory.execute(
                new DeleteCategoryCommand(userId, CategoryId.fromString(categoryId))
        );

        // du kannst 204 machen; Output DTO ist optional
        return Response.noContent().build();
    }
    @POST
    @Path("/change-color")
    public Response changeColor(
            @HeaderParam("X-User-Id") String userIdHeader,
            @QueryParam("categoryId") String categoryId,
            ChangeCategoryColorHttpRequest request
    ) {
        UserId userId = UserId.fromString(requireHeader(userIdHeader, "X-User-Id"));

        if (categoryId == null || categoryId.isBlank()) {
            throw new IllegalArgumentException("categoryId query parameter is required");
        }

        if (request == null) request = new ChangeCategoryColorHttpRequest();

        ChangeCategoryColorOutputDTO output = changeCategoryColor.execute(
                new ChangeCategoryColorCommand(
                        userId,
                        CategoryId.fromString(categoryId),
                        request.color
                )
        );

        return presenter.present(output);
    }
}
