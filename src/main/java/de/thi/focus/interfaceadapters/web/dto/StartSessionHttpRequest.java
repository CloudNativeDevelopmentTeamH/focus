package de.thi.focus.interfaceadapters.web.dto;

public class StartSessionHttpRequest {

    public String startedAt;   // ISO-8601, optional
    public String categoryId;  // UUID string, optional
    public String note;        // optional
}
