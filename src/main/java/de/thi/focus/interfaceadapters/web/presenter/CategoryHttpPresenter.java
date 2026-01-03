package de.thi.focus.interfaceadapters.web.presenter;

import de.thi.focus.usecases.dtos.output.*;

import jakarta.ws.rs.core.Response;

public interface CategoryHttpPresenter {
    Response present(CreateCategoryOutputDTO output);
    Response present(RenameCategoryOutputDTO output);
    Response present(ArchiveCategoryOutputDTO output);
    Response present(ListCategoriesOutputDTO output);
    Response present(ChangeCategoryColorOutputDTO output);
    Response present(UnarchiveCategoryOutputDTO output);
}
