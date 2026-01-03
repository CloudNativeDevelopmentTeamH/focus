package de.thi.focus.interfaceadapters.web.presenter;

import de.thi.focus.usecases.dtos.output.GetRunningSessionOutputDTO;
import de.thi.focus.usecases.dtos.output.ResumeSessionOutputDTO;
import de.thi.focus.usecases.dtos.output.StartSessionOutputDTO;
import de.thi.focus.usecases.dtos.output.StopSessionOutputDTO;

import jakarta.ws.rs.core.Response;

public interface SessionHttpPresenter {

    Response present(StartSessionOutputDTO output);

    Response present(ResumeSessionOutputDTO output);

    Response present(StopSessionOutputDTO output);

    Response present(GetRunningSessionOutputDTO output);
}
