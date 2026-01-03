package de.thi.focus.interfaceadapters.web.presenter;

import de.thi.focus.usecases.dtos.output.ArchiveCategoryOutputDTO;
import de.thi.focus.usecases.dtos.output.CreateCategoryOutputDTO;
import de.thi.focus.usecases.dtos.output.ListCategoriesOutputDTO;
import de.thi.focus.usecases.dtos.output.RenameCategoryOutputDTO;

import jakarta.ws.rs.core.Response;

public interface CategoryHttpPresenter {
    Response present(CreateCategoryOutputDTO output);
    Response present(RenameCategoryOutputDTO output);
    Response present(ArchiveCategoryOutputDTO output);
    Response present(ListCategoriesOutputDTO output);
}
