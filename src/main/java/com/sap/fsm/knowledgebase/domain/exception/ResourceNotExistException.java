package com.sap.fsm.knowledgebase.domain.exception;

import org.springframework.http.HttpStatus;


public class ResourceNotExistException extends BusinessException {
    private static final long serialVersionUID = 1L;
    private static final String reason = "Resource not found";
    private static final String errorMsg = "Resource not found for identifier: %s";

    public ResourceNotExistException(String id) {
        super(String.format(errorMsg, id));
    }

    @Override
    public final HttpStatus getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public final String getReason() {
        return reason;
    }

}
