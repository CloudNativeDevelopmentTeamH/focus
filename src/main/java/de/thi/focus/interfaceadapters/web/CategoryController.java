package de.thi.focus.interfaceadapters.web;

import de.thi.focus.entities.ids.CategoryId;
import de.thi.focus.entities.ids.UserId;
import de.thi.focus.interfaceadapters.web.dto.ChangeCategoryColorHttpRequest;
import de.thi.focus.interfaceadapters.web.dto.CreateCategoryHttpRequest;
import de.thi.focus.interfaceadapters.web.dto.RenameCategoryHttpRequest;
import de.thi.focus.interfaceadapters.web.presenter.CategoryHttpPresenter;
import de.thi.focus.interfaceadapters.web.security.CurrentUser;
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
    private final CurrentUser currentUser;

    public CategoryController(
            CreateCategoryInputPort createCategory,
            RenameCategoryInputPort renameCategory,
            ArchiveCategoryInputPort archiveCategory,
            DeleteCategoryInputPort deleteCategory,
            ListCategoriesInputPort listCategories,
            CategoryHttpPresenter presenter,
            ChangeCategoryColorInputPort changeCategoryColor,
            UnarchiveCategoryInputPort unarchiveCategory,
            CurrentUser currentUser
    ) {
        this.createCategory = Objects.requireNonNull(createCategory);
        this.renameCategory = Objects.requireNonNull(renameCategory);
        this.archiveCategory = Objects.requireNonNull(archiveCategory);
        this.deleteCategory = Objects.requireNonNull(deleteCategory);
        this.listCategories = Objects.requireNonNull(listCategories);
        this.presenter = Objects.requireNonNull(presenter);
        this.changeCategoryColor = Objects.requireNonNull(changeCategoryColor);
        this.unarchiveCategory = Objects.requireNonNull(unarchiveCategory);
        this.currentUser = Objects.requireNonNull(currentUser);
    }

    @POST
    @Path("/create")
    public Response create(CreateCategoryHttpRequest request) {
        UserId userId = currentUser.userId();

        if (request == null) request = new CreateCategoryHttpRequest();

        CreateCategoryOutputDTO output = createCategory.execute(
                new CreateCategoryCommand(userId, request.name, request.color)
        );

        return presenter.present(output);
    }

    @POST
    @Path("/rename")
    public Response rename(
            @QueryParam("categoryId") String categoryId,
            RenameCategoryHttpRequest request
    ) {
        UserId userId = currentUser.userId();

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
    public Response archive(@QueryParam("categoryId") String categoryId) {
        UserId userId = currentUser.userId();

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
    public Response unarchive(@QueryParam("categoryId") String categoryId) {
        UserId userId = currentUser.userId();

        if (categoryId == null || categoryId.isBlank()) {
            throw new IllegalArgumentException("categoryId query parameter is required");
        }

        UnarchiveCategoryOutputDTO output = unarchiveCategory.execute(
                new UnarchiveCategoryCommand(userId, CategoryId.fromString(categoryId))
        );

        return presenter.present(output);
    }

    @GET
    public Response list() {
        UserId userId = currentUser.userId();

        ListCategoriesOutputDTO output = listCategories.execute(userId);
        return presenter.present(output);
    }

    @POST
    @Path("/delete")
    public Response delete(@QueryParam("categoryId") String categoryId) {
        UserId userId = currentUser.userId();

        if (categoryId == null || categoryId.isBlank()) {
            throw new IllegalArgumentException("categoryId query parameter is required");
        }

        deleteCategory.execute(
                new DeleteCategoryCommand(userId, CategoryId.fromString(categoryId))
        );

        return Response.noContent().build();
    }

    @POST
    @Path("/change-color")
    public Response changeColor(
            @QueryParam("categoryId") String categoryId,
            ChangeCategoryColorHttpRequest request
    ) {
        UserId userId = currentUser.userId();

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
