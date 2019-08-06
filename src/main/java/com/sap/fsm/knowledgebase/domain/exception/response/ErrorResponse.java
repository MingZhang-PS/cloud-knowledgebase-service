package com.sap.fsm.knowledgebase.domain.exception.response;

import org.springframework.http.HttpStatus;
import java.util.HashMap;

public class ErrorResponse extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;
    // Refer
    // https://github.com/coresystemsFSM/documentation/wiki/API-Guidelines#must-use-problem-details-for-http-apis
    private static final String TYPE = "type";
    private static final String TITLE = "title";
    private static final String STATUS = "status";
    private static final String DETAIL = "detail";

    public ErrorResponse() {
        super();
        this.put(TYPE, "about:blank");
        this.put(STATUS, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public void setType(String type) {
        this.put(TYPE, type);
    }

    public void setStatus(HttpStatus status) {
        this.put(STATUS, status.value());
    }

    public void setDetail(String detail) {
        this.put(DETAIL, detail);
    }

    public void setTitle(String title) {
        this.put(TITLE, title);
    }
}