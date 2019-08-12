package com.sap.fsm.knowledgebase.domain.exception;

import org.springframework.http.HttpStatus;

public abstract class ResourcePresentException extends BusinessException {
    private static final long serialVersionUID = 1L;
    private static final String reason = "Resource exists";

    public ResourcePresentException(String errMsg) {
        super(errMsg);
    }

    @Override
    public final HttpStatus getStatusCode() {
        return HttpStatus.CONFLICT;
    }

    @Override
    public final String getReason() {
        return reason;
    }
}
